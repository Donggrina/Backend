package com.codeit.donggrina.domain.calendar.repository;


import static com.codeit.donggrina.domain.calendar.entity.QCalendar.calendar;
import static com.codeit.donggrina.domain.member.entity.QMember.member;
import static com.codeit.donggrina.domain.pet.entity.QPet.pet;

import com.codeit.donggrina.domain.calendar.dto.response.CalendarDetailResponse;
import com.codeit.donggrina.domain.calendar.entity.Calendar;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomCalendarRepositoryImpl implements CustomCalendarRepository {

    private final JPAQueryFactory queryFactory;

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
