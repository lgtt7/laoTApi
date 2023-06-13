package com.laot.laotapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class SignUtils {
    public static String getSign(Map<String,String> map, String secretKey){
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = map.toString() + "." + secretKey;
        return digester.digestHex(content);
    }


    public static Map<String,Object> jsonToMap(String jsonStr){
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        return gson.fromJson(jsonStr, type);
    }
}
