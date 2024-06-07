package com.codeit.donggrina.domain.calendar.repository;

import com.codeit.donggrina.domain.calendar.dto.response.CalendarDetailResponse;

public interface CustomCalendarRepository {

    CalendarDetailResponse getDetail(Long calendarId);
}
