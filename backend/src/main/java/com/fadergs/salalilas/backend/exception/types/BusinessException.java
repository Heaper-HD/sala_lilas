package com.fadergs.salalilas.backend.exception.types;

import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.SalaLilasException;
import org.springframework.http.HttpStatus;

public class BusinessException extends SalaLilasException {
    public BusinessException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode, HttpStatus.UNPROCESSABLE_ENTITY, message);
    }
}
