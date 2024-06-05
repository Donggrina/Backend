package com.codeit.donggrina.domain.calendar.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class IsoDateTimeValidator implements ConstraintValidator<IsoDateTime, String> {

    @Override
    public void initialize(IsoDateTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            LocalDateTime.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
