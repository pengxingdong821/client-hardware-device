package com.fontre.clienthardwaredevice.util;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static final ObjectMapper JSON = new ObjectMapper();

    static {
        JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JSON.configure(SerializationFeature.INDENT_OUTPUT, Boolean.TRUE);
    }

    public static String toJson(Object obj) {
        try {
            return "{\"data\":" + JSON.writeValueAsString(obj) + ", \"error_code\" : 0}";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String toJson() {
        return "{\"data\":\"\", \"error_code\" : 0}";
    }

    public static String toSuccessJson(HttpStatus httpStatus) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("message", httpStatus.getReasonPhrase());
        map.put("status", httpStatus.value());
        return JSONUtil.parseObj(map).toString();
    }


    public static String toJson(String msg, int errorCode) {
        return "{\"msg\":" + "\"" + msg + "\"" + ", \"error_code\" : " + errorCode + "}";
    }

    public static Map<String,Object> toMap(String msg, int errorCode) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("msg", msg);
        map.put("error_code", errorCode);
        return map;
    }

    public static Map<String, Object> toErrorMessageMap(String detail, int errorCode) {
        String msg = ErrorCodeMsg.getCodeMsg(errorCode, detail);
        return toMap(msg, errorCode);
    }

    public static ResponseEntity<Object> toErrorMessageResponseEntity(String detail, int errorCode) {
        Map<String, Object> map = toErrorMessageMap(detail, errorCode);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public static String toErrorMessageJson(String detail, int errorCode) {
        String result = ErrorCodeMsg.getCodeMsg(errorCode, detail);
        return toJson(result, errorCode);
    }

    public static ResponseEntity<Object> getEmptyPageResult() {
        return new ResponseEntity<>(getEmptyPageMap(), HttpStatus.OK);
    }

    public static Map<String, Object> getEmptyPageMap() {
        List<Object> list = new ArrayList<>();
        Map<String, Object> map = PageUtil.toPage(list, list.size());
        return map;
    }

    public static String toJson(Object obj, int totalElements) {
        try {
            return "{\"content\":" + JSON.writeValueAsString(obj) + ", \"totalElements\" : " + totalElements + "}";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String toJson(String result) {
       return toJson(result, 100);
    }

}