package com.mtjsoft.www.kotlinmvputils.rxhttp.entity;

public class Response<T> {

    private boolean Result = false;
    private String info = "";
    private int code = -1;
    private T data;

    public int getCode() {
        return code;
    }

    public void setErrorCode(int errorCode) {
        this.code = errorCode;
    }

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean result) {
        Result = result;
    }

    public String getMsg() {
        return info == null ? "" : info;
    }

    public void setMsg(String msg) {
        info = msg;
    }

    public int getErrorCode() {
        return code;
    }

    public T getDatas() {
        return data;
    }

    public void setDatas(T datas) {
        data = datas;
    }

    public boolean isSuccess() {
        return code == 200 || Result;
    }
}
