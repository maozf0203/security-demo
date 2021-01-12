package com.example.compent.exception;


public class BaseException extends RuntimeException{
    private ErrorType errorType;
    public ErrorType getErrorType() {
        return this.errorType;
    }
}
