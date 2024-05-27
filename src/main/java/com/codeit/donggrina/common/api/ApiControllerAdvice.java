package com.codeit.donggrina.common.api;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    /**
     * MethodArgumentNotValidException 발생 시, 유효성 검사 실패 응답을 반환합니다.
     *
     * @param e MethodArgumentNotValidException
     * @return ErrorResponse
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorResponse validationErrorHandler(MethodArgumentNotValidException e) {
        ValidationErrorResponse response = ValidationErrorResponse.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message("잘못된 요청입니다.")
            .build();
        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }

    /**
     * IllegalArgumentException 발생 시, 잘못된 요청에 대한 응답을 반환합니다.
     *
     * @param e IllegalArgumentException
     * @return ErrorResponse
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse invalidRequestHandler(IllegalArgumentException e) {
        return ErrorResponse.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(e.getMessage())
            .build();
    }
}
