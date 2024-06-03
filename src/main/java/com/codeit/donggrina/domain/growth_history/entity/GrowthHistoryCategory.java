package com.codeit.donggrina.domain.growth_history.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GrowthHistoryCategory {
    FOOD("사료"),
    SNACK("간식"),
    ABNORMAL_SYMPTOM("이상 증상"),
    HOSPITAL("병원 기록");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static GrowthHistoryCategory fromValue(String value) {
        for (GrowthHistoryCategory growthHistoryCategory : values()) {
            if (growthHistoryCategory.value.equals(value)) {
                return growthHistoryCategory;
            }
        }
        throw new IllegalArgumentException("카테고리를 잘못 입력했습니다.");
    }
}
