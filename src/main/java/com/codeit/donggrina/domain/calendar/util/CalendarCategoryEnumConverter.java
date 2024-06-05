package com.codeit.donggrina.domain.calendar.util;

import com.codeit.donggrina.domain.calendar.entity.CalendarCategory;
import jakarta.persistence.AttributeConverter;

public class CalendarCategoryEnumConverter implements AttributeConverter<CalendarCategory, String> {

    @Override
    public String convertToDatabaseColumn(CalendarCategory calendarCategory) {
        return calendarCategory == null ? null : calendarCategory.getValue();
    }

    @Override
    public CalendarCategory convertToEntityAttribute(String s) {
        return CalendarCategory.fromValue(s);
    }
}
