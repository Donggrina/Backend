package com.codeit.donggrina.domain.calendar.repository;

import com.codeit.donggrina.domain.calendar.dto.response.CalendarDetailResponse;
import com.codeit.donggrina.domain.calendar.dto.response.CalendarListResponse;
import java.time.LocalDate;
import java.util.List;

public interface CustomCalendarRepository {

    List<CalendarListResponse> getDayListByDate(Long groupId, LocalDate date);

    CalendarDetailResponse getDetail(Long calendarId);
}
