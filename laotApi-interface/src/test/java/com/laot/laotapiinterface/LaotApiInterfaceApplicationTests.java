package com.laot.laotapiinterface;

import com.laot.laotapiclientsdk.client.LaoTApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LaotApiInterfaceApplicationTests {


    @Autowired
    private LaoTApiClient laoTApiClient;

    @Test
    void contextLoads() {

    }

}
