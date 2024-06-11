package com.codeit.donggrina.domain.calendar.repository;

import com.codeit.donggrina.common.api.SearchFilter;
import com.codeit.donggrina.domain.calendar.dto.response.CalendarDailyCountResponse;
import com.codeit.donggrina.domain.calendar.entity.Calendar;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface CustomCalendarRepository {

    List<CalendarDailyCountResponse> getDailyCountByMonth(Long groupId, YearMonth yearMonth);

    List<Calendar> getDayListByDate(Long groupId, LocalDate date);

    Calendar getDetail(Long calendarId);

    List<Calendar> findBySearchFilter(SearchFilter searchFilter);
}
