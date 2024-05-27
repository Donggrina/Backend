package com.codeit.donggrina.common.api;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;

public record ValidationErrorResponse(int code, String message, Map<String, String> validation) {

    @Builder
    public ValidationErrorResponse(int code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
