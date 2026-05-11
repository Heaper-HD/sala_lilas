package com.fadergs.salalilas.backend.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class SalaLilasException extends RuntimeException {
    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;

    public SalaLilasException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public SalaLilasException(ErrorCode errorCode, HttpStatus httpStatus, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
