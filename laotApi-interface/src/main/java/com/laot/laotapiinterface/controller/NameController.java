package com.laot.laotapiinterface.controller;

import com.laot.laotapiclientsdk.model.User;
import com.laot.laotapiinterface.model.BaseResponse;
import com.laot.laotapiinterface.model.ResultUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * API接口
 *
 * @author laoT
 */
@RestController
@RequestMapping("/name")
public class NameController {
    @GetMapping("/")
    public String getNameByGet(String name){
        return "(GET)你的名字是："+name;
    }

    @PostMapping("/")
    public String getNameByPost(@RequestParam String name){
        return "(POST)你的名字是："+name;
    }
    @PostMapping("/username")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request){

        return "(POST2)你的用户名是："+user.getUsername();
    }


    @PostMapping("/laot")
    public BaseResponse<User> laotDemo(){
        User user = new User();
        user.setUsername("laot");
        return ResultUtils.success(user);
    }

}
