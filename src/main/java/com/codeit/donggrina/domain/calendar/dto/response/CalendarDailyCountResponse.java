package com.codeit.donggrina.domain.calendar.dto.response;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class CalendarDailyCountResponse {

    private final String date;
    private final long count;

    public CalendarDailyCountResponse(int year, int month, int dayOfMonth, long count) {
        this.date = LocalDate.of(year, month, dayOfMonth).toString();
        this.count = count;
    }
}
