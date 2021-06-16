package com.lyz.practice.common;

/**
 * @author liaoyz
 * @version v1.0
 * @description: 类说明 （必填）
 * @date 2021/06/16 14:37
 * @change 2021/06/16 14:37 liaoyz@v1.0 创建
 */
public class Result<T> {
    private Integer code = 200;
    private String msg = "SUCCESS";
    private T data;

    public Result() {
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public Result<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}
