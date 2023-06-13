package com.laot.laotapiclientsdk;

import com.laot.laotapiclientsdk.client.LaoTApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("laotapi.client")
@Data
@ComponentScan("com.laot.laotapiclientsdk")
public class laotApiClientSdkConfig {

    private String accessKey;
    private String secretKey;


    @Bean
    public LaoTApiClient laotApiClient(){
        return new LaoTApiClient(accessKey, secretKey);

    }
}
