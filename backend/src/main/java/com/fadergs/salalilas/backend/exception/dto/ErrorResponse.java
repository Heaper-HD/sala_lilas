package com.fadergs.salalilas.backend.exception.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String errorCode;
    private String message;
    private int httpStatus;
    private String path;
    private LocalDateTime timestamp;
    private List<FieldError> fields;

    @Data
    @Builder
    public static class FieldError {
        private String field;
        private String message;
        private Object rejectedValue;
    }
}
