package com.codeit.donggrina.domain.pet.entity;

import jakarta.persistence.AttributeConverter;

public class SexValueConverter implements AttributeConverter<Sex, String> {

    @Override
    public String convertToDatabaseColumn(Sex sex) {
        return sex == null ? null : sex.getValue();
    }

    @Override
    public Sex convertToEntityAttribute(String s) {
        return Sex.fromValue(s);
    }
}
