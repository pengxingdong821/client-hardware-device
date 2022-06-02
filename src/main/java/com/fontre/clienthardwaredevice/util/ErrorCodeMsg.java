package com.fontre.clienthardwaredevice.util;


import java.util.HashMap;
import java.util.Map;

public class ErrorCodeMsg {

    static Map<Integer, String> codeMsgs = new HashMap<>();

    public static int USERACCESSTOKENNOTEXIST = 100;
    public static int USERMOBILENOTEXIST = 101;

    static {
        codeMsgs.put(USERMOBILENOTEXIST, "手机号不存在");
    }


    public static String getCodeMsg(int code){
        if(codeMsgs.containsKey(code)){
            return codeMsgs.get(code);
        } else {
            return "错误信息未定义";
        }
    }

    public static final int REQUEST_PARAMETER_ILLEGAL = 500;//请求参数非法
    public static final int DATABASE_ERROR = 600;//数据库错误
    public static final int SERVER_EXCEPTION = 700;//服务端异常
    public static final int HTTP_OK = 200;//请求成功

    public static String getCodeMsg(int code, String detail) {
        switch (code) {
            case REQUEST_PARAMETER_ILLEGAL: return "请求参数非法：" + detail;
            case DATABASE_ERROR: return "数据库错误: " + detail;
            case SERVER_EXCEPTION: return "服务端异常: " + detail;
            default: return "错误信息未定义";
        }
    }
}
