package com.hdsx.rabbitmq.util;

import com.google.gson.Gson;

/**
 * @author huyue@hdsxtech.com
 * @date 2018/6/21 15:23
 */
public class GsonUtil {

    private static final Gson googleJson = new Gson();

    /**
     * 获取Gson常量，减少重复创建,在编译后就完成改变量初始化
     * @return
     */
    public static Gson getGoogleJson(){
        return googleJson;
    }

    /**
     * 直接获取
     * @param <T>
     * @param json
     * @param class1
     * @return
     */
    public static <T> Object jsonToObject(String json , Class<T> class1){
        return googleJson.fromJson(json, class1);
    }

    public static String objectToJson(Object object){
        return googleJson.toJson(object);
    }
}
