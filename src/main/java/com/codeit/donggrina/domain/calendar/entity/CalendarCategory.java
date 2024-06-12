package com.codeit.donggrina.domain.calendar.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CalendarCategory {
    WALK("산책"),
    FOOD("사료 구입"),
    BATH("목욕"),
    HOSPITAL("병원"),
    MEDICATION("투약"),
    BEAUTY("미용"),
    CELEBRATION("기념"),
    ETC("기타");

    private final String value;

    @JsonCreator
    public static CalendarCategory fromValue(String value) {
        for (CalendarCategory calendarCategory : values()) {
            if (calendarCategory.value.equals(value)) {
                return calendarCategory;
            }
        }
        throw new IllegalArgumentException("카테고리를 잘못 입력했습니다.");
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
