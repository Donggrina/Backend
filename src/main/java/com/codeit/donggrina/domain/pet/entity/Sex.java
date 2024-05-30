package com.codeit.donggrina.domain.pet.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Sex {
    MALE("수컷"),
    FEMALE("암컷");

    private final String value;

    Sex(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Sex fromValue(String value) {
        for (Sex sex : values()) {
            if (sex.value.equals(value)) {
                return sex;
            }
        }
        throw new IllegalArgumentException("성별을 잘못 입력했습니다.");
    }

}
