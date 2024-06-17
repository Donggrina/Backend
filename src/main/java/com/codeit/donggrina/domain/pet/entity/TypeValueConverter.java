package com.codeit.donggrina.domain.pet.entity;

import jakarta.persistence.AttributeConverter;

public class TypeValueConverter implements AttributeConverter<Type, String> {

    @Override
    public String convertToDatabaseColumn(Type type) {
        return type == null ? null : type.getValue();
    }

    @Override
    public Type convertToEntityAttribute(String s) {
        return Type.fromValue(s);
    }
}
