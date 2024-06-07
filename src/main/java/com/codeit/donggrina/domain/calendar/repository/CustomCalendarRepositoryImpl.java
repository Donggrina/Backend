package com.codeit.donggrina.domain.calendar.repository;


import static com.codeit.donggrina.domain.calendar.entity.QCalendar.calendar;
import static com.codeit.donggrina.domain.member.entity.QMember.member;
import static com.codeit.donggrina.domain.pet.entity.QPet.pet;

import com.codeit.donggrina.domain.calendar.dto.response.CalendarDetailResponse;
import com.codeit.donggrina.domain.calendar.dto.response.CalendarListResponse;
import com.codeit.donggrina.domain.calendar.entity.Calendar;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomCalendarRepositoryImpl implements CustomCalendarRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CalendarListResponse> getDayListByDate(Long groupId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // 조회하려는 날짜의 00:00:00
        LocalDateTime endOfDay = date.atTime(23, 59, 59); // 조회하려는 날짜의 23:59:59

        return queryFactory
            .selectFrom(calendar)
            .leftJoin(calendar.member, member).fetchJoin()
            .leftJoin(member.profileImage).fetchJoin()
            .leftJoin(calendar.pet, pet).fetchJoin()
            .leftJoin(pet.profileImage).fetchJoin()
            .where(
                calendar.member.group.id.eq(groupId)
                    .and(calendar.dateTime.between(startOfDay, endOfDay))
            )
            .orderBy(calendar.id.desc())
            .fetch()
            .stream()
            .map(CalendarListResponse::from)
            .toList();
    }

    @Override
    public CalendarDetailResponse getDetail(Long calendarId) {
        Calendar findCalendar = queryFactory
            .selectFrom(calendar)
            .leftJoin(calendar.member, member).fetchJoin()
            .leftJoin(member.profileImage).fetchJoin()
            .leftJoin(calendar.pet, pet).fetchJoin()
            .leftJoin(pet.profileImage).fetchJoin()
            .where(calendar.id.eq(calendarId))
            .fetchOne();
        if (findCalendar == null) {
            throw new IllegalArgumentException("존재하지 않는 일정입니다.");
        }
        return CalendarDetailResponse.from(findCalendar);
    }
}
