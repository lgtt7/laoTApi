package com.laot.project.provider.Impl;

import com.laot.project.innerInterface.DemoService;
import org.apache.dubbo.config.annotation.DubboService;


@DubboService
public class DemoServiceServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return "Hello!"+name;
    }

    @Override
    public String sayHello2(String name) {
        return "laot";
    }
}
