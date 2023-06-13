package com.laot.laotapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;


import java.util.HashMap;
import java.util.Map;

import static com.laot.laotapiclientsdk.utils.SignUtils.getSign;
import static com.laot.laotapiclientsdk.utils.SignUtils.jsonToMap;

/**
 * 调用第三方接口
 *
 * @author Laot
 */
public class LaoTApiClient {
    private String accessKey;
    private String secretKey;
    private static final String GATEWAY_HOST = "http://localhost:8090";

    public LaoTApiClient(String accessKey,String secretKey){
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }



    private Map<String,String> getHeaderMap(String body){
        Map<String,String> map = new HashMap<>();
        map.put("accessKey",accessKey);
        //不能直接发送sk
//        map.put("secretKey",secretKey);
        map.put("nonce", RandomUtil.randomNumbers(4));
        if(body==null){
            body = "";
        }
        map.put("body",body);
        map.put("timeStamp",String.valueOf(System.currentTimeMillis()/1000));
        map.put("Sign",getSign(map,secretKey));
        return map;
    }


    public String getInvoke(String paramsJson,String url){
        Map<String, Object> param = jsonToMap(paramsJson);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(GATEWAY_HOST);
        stringBuffer.append(url);
        stringBuffer.append("/");
        stringBuffer.append("?");
        for(String key : param.keySet()){
            stringBuffer.append(key+"="+param.get(key)+"&");
        }
        int length = stringBuffer.length();
        if(stringBuffer.charAt(length-1)=='&'){
            stringBuffer.deleteCharAt(length-1);
        }
        url = stringBuffer.toString();
        HttpResponse res = HttpUtil.createGet(url)
                .addHeaders(getHeaderMap(paramsJson))
                .body(paramsJson)
                .execute()
                .charset("utf-8");

        String body = res.body();
        return body;
    }


    public String postInvoke(String paramsJson,String url){
        HttpResponse execute = HttpRequest.post(GATEWAY_HOST+url)
                .addHeaders(getHeaderMap(paramsJson))
                .body(paramsJson)
                .execute();
        String body = execute.body();
        return body;
    }



}
