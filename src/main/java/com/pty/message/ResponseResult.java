package com.pty.message;

/**
 * 响应模板
 * @author : pety
 * @date : 2022/7/16 16:29
 */

public enum ResponseResult {
    SUCCESS(200,"请求成功"),
    FAIL(500,"请求失败");


    private int statusCode;
    private String msg;


    ResponseResult(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }


    public String getMsg() {
        return msg;
    }
}
