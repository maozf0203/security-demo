package com.example.compent.exception;

public enum SystemErrorType implements ErrorType{
    SYSTEM_ERROR("-1","系统异常"),
    INVALID_TOKEN("",""),
    FORBIDDEN("",""),
    GET_TOKEN_KEY_ERROR("","权限不足"),
    UNAUTHORIZED_HEADER_IS_EMPTY("","权限不足"),
    GATEWAY_CONNECT_TIME_OUT("","权限不足"),
    GATEWAY_NOT_FOUND_SERVICE("","权限不足"),





    GATEWAY_ERROR("","权限不足")



        ;
    private String code;

    private String msg;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    SystemErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

