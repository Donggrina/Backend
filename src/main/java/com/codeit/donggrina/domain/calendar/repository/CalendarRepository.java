package com.codeit.donggrina.domain.calendar.repository;

import com.codeit.donggrina.domain.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

}
