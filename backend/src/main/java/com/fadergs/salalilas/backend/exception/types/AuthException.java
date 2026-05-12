package com.fadergs.salalilas.backend.exception.types;

import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.SalaLilasException;
import org.springframework.http.HttpStatus;

public class AuthException extends SalaLilasException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.UNAUTHORIZED);
    }

    public AuthException(ErrorCode errorCode, String message) {
        super(errorCode, HttpStatus.UNAUTHORIZED, message);
    }
}
