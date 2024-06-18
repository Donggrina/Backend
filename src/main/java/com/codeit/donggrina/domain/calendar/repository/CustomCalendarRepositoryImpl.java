package com.codeit.donggrina.domain.calendar.repository;


import static com.codeit.donggrina.domain.calendar.entity.QCalendar.calendar;
import static com.codeit.donggrina.domain.member.entity.QMember.member;
import static com.codeit.donggrina.domain.pet.entity.QPet.pet;

import com.codeit.donggrina.common.api.SearchFilter;
import com.codeit.donggrina.domain.calendar.dto.response.CalendarDailyCountResponse;
import com.codeit.donggrina.domain.calendar.entity.Calendar;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomCalendarRepositoryImpl implements CustomCalendarRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CalendarDailyCountResponse> getDailyCountByMonth(Long groupId,
        YearMonth yearMonth) {

        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        LocalDateTime startOfDay = startOfMonth.atStartOfDay();
        LocalDateTime endOfDay = endOfMonth.atTime(23, 59, 59);

        return queryFactory
            .select(Projections.constructor(CalendarDailyCountResponse.class,
                calendar.dateTime.year(),
                calendar.dateTime.month(),
                calendar.dateTime.dayOfMonth(),
                calendar.id.count()
            ))
            .from(calendar)
            .where(calendar.member.group.id.eq(groupId)
                .and(calendar.dateTime.between(startOfDay, endOfDay))
            )
            .groupBy(
                calendar.dateTime.year(),
                calendar.dateTime.month(),
                calendar.dateTime.dayOfMonth()
            )
            .fetch();
    }

    @Override
    public List<Calendar> getDayListByDate(Long groupId, LocalDate date) {
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
            .fetch();
    }

    @Override
    public Calendar getDetail(Long calendarId) {
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
        return findCalendar;
    }

    @Override
    public List<Calendar> findBySearchFilter(Long groupId, SearchFilter searchFilter) {
        return queryFactory
            .selectFrom(calendar)
            .leftJoin(calendar.member, member).fetchJoin()
            .leftJoin(member.profileImage).fetchJoin()
            .leftJoin(calendar.pet, pet).fetchJoin()
            .leftJoin(pet.profileImage).fetchJoin()
            .where(
                containsKeyword(searchFilter.keyword()),
                inPetNames(searchFilter.petNames()),
                inWriterNames(searchFilter.writerNames()),
                calendar.member.group.id.eq(groupId)
            )
            .orderBy(calendar.id.desc())
            .fetch();
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        return calendar.title.contains(keyword)
            .or(calendar.memo.contains(keyword));
    }

    private BooleanExpression inPetNames(List<String> petNames) {
        return petNames.isEmpty() ? null : pet.name.in(petNames);
    }

    private BooleanExpression inWriterNames(List<String> writerNames) {
        return writerNames.isEmpty() ? null : member.nickname.in(writerNames);
    }
}
