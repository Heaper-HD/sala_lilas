package com.fadergs.salalilas.backend.exception.types;

import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.SalaLilasException;
import org.springframework.http.HttpStatus;

public class AccessDeniedException extends SalaLilasException {

    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.FORBIDDEN);
    }
}