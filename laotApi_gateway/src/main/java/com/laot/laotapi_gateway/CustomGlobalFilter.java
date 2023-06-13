package com.laot.laotapi_gateway;

import com.laot.laotapiclientsdk.utils.SignUtils;
import com.laot.project.innerInterface.InnerInvoke;
import com.laot.project.model.entity.InterfaceInfo;
import com.laot.project.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    static final Long FIVE_MINUTES = 5*60L;


    @DubboReference
    private InnerInvoke innerInvoke;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //请求日志
        ServerHttpRequest request = exchange.getRequest();
        //用户鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("Sign");
        String body = headers.getFirst("body");
        String path = request.getPath().value();
        String method = request.getMethod().toString();

        ServerHttpResponse response = exchange.getResponse();

        if(Long.parseLong(nonce)>10000L){
            return handleNoAuth(response);
        }
        User user = null;
        //到数据库去查询用户是否存在
        try {
            user = innerInvoke.isHavingUser(accessKey);
            if(user==null){
                //用户不存在
                return handleNoAuth(response);
            }
        }catch (Exception e){
            log.error("isHavingUser时报错",e);
        }


        //判断调用时间和当前时间不能大于5分钟
        Long currentTimeMillis = System.currentTimeMillis() / 1000;

        if((currentTimeMillis-Long.parseLong(timestamp))>FIVE_MINUTES){
            return handleNoAuth(response);
        }

        //判断secretKey是否正确 实际上是查询数据库
        String inDataSecreteKey = user.getSecretKey();
        Map<String,String> map = new HashMap<>();
        map.put("accessKey",accessKey);
        //不能直接发送sk
//        map.put("secretKey",secretKey);
        map.put("nonce", nonce);
        map.put("body",body);
        map.put("timeStamp",timestamp);
        String newSign = SignUtils.getSign(map, inDataSecreteKey);
        if(!newSign.equals(sign)) {
            return handleNoAuth(response);
        }


        //请求的模拟接口是否存在
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInvoke.isHavingInterface(path, method);
            if(interfaceInfo==null){
                return handleNoAuth(response);
            }
        } catch (Exception e){
            log.error("isHavingInterface时报错",e);
        }

        // 判断是否还有调用次数
        try {
            boolean isHavingCount = innerInvoke.isHavingInvokeCount(user.getId(), interfaceInfo.getId());
            if(!isHavingCount){
                return handleNoAuth(response);
            }
        } catch (Exception e){
            log.error("isHavingInvokeCount时报错",e);
        }

        //请求转发，调用接口
        return handleResponseLog(exchange,chain, user.getId(), interfaceInfo.getId());
    }

    /**
     * 处理响应
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponseLog(ServerWebExchange exchange, GatewayFilterChain chain,Long userId,Long interfaceInfoId){
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            //缓冲区工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            //拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                //装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    //等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        //log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //往返回值里面写数据
                            //拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        //8.调用成功，接口调用次数+1 invokeCount
                                        try {
                                            innerInvoke.invokeCount(userId, interfaceInfoId);
                                        } catch (Exception e) {
                                            log.error("invokeCount出错!",e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        sb2.append("<--- {} {} \n");
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        //rspArgs.add(requestUrl);
                                        String data = new String(content, StandardCharsets.UTF_8);//data
                                        sb2.append(data);
                                        //打印日志
                                        log.info(sb2.toString(), rspArgs.toArray(), data);//log.info("<-- {} {}\n", originalResponse.getStatusCode(), data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                //设置response对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应错误.\n" + e);
            return chain.filter(exchange);
        }
    }


    @Override
    public int getOrder() {
        return -2;
    }


    public Mono<Void> handleNoAuth(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}
