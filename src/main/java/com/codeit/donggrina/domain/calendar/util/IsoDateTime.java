package com.codeit.donggrina.domain.calendar.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IsoDateTimeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsoDateTime {

    String message() default "날짜와 시간 형식을 올바르게 입력해주세요. (예시: 2024-06-05T09:00)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
