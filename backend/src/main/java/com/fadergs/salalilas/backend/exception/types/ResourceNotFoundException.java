package com.fadergs.salalilas.backend.exception.types;

import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.SalaLilasException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends SalaLilasException {
    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.NOT_FOUND);
    }
}
