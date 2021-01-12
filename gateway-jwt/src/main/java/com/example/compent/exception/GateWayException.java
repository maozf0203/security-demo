package com.example.compent.exception;

import lombok.Data;

@Data
public class GateWayException extends RuntimeException {

    private String code;

    private String msg;

    public GateWayException(SystemErrorType systemErrorType) {
        this.code = systemErrorType.getCode();
        this.msg = systemErrorType.getMsg();
    }

}
