package com.codeit.donggrina.domain.growth_history.repository;

import static com.codeit.donggrina.domain.growth_history.entity.QGrowthHistory.growthHistory;
import static com.codeit.donggrina.domain.member.entity.QMember.member;
import static com.codeit.donggrina.domain.pet.entity.QPet.pet;

import com.codeit.donggrina.common.api.SearchFilter;
import com.codeit.donggrina.domain.growth_history.entity.GrowthHistory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomGrowthHistoryRepositoryImpl implements CustomGrowthHistoryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GrowthHistory> findGrowthHistoryDetailByDate(Long groupId, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return queryFactory
            .selectFrom(growthHistory)
            .leftJoin(growthHistory.member, member).fetchJoin()
            .leftJoin(member.profileImage).fetchJoin()
            .leftJoin(growthHistory.pet, pet).fetchJoin()
            .leftJoin(pet.profileImage).fetchJoin()
            .where(
                growthHistory.date.eq(date)
                    .and(member.group.id.eq(groupId))
            )
            .orderBy(growthHistory.id.desc())
            .fetch();
    }

    @Override
    public GrowthHistory findGrowthHistoryDetail(Long growthId) {
        GrowthHistory findGrowthHistory = queryFactory
            .selectFrom(growthHistory)
            .leftJoin(growthHistory.member, member).fetchJoin()
            .leftJoin(member.profileImage).fetchJoin()
            .leftJoin(growthHistory.pet, pet).fetchJoin()
            .leftJoin(pet.profileImage).fetchJoin()
            .where(growthHistory.id.eq(growthId))
            .fetchOne();
        if (findGrowthHistory == null) {
            throw new IllegalArgumentException("존재하지 않는 성장기록입니다.");
        }
        return findGrowthHistory;
    }

    @Override
    public List<GrowthHistory> findGrowthHistoryBySearchFilter(
        Long groupId,
        SearchFilter searchFilter) {
        return queryFactory
            .selectFrom(growthHistory)
            .leftJoin(growthHistory.member, member).fetchJoin()
            .leftJoin(member.profileImage).fetchJoin()
            .leftJoin(growthHistory.pet, pet).fetchJoin()
            .leftJoin(pet.profileImage).fetchJoin()
            .where(
                containsKeyword(searchFilter.keyword()),
                inPetNames(searchFilter.petNames()),
                inWriterNames(searchFilter.writerNames()),
                member.group.id.eq(groupId)
            )
            .orderBy(growthHistory.id.desc())
            .fetch();
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        return growthHistory.food.containsIgnoreCase(keyword)
            .or(growthHistory.snack.containsIgnoreCase(keyword))
            .or(growthHistory.abnormalSymptom.containsIgnoreCase(keyword))
            .or(growthHistory.hospitalName.containsIgnoreCase(keyword))
            .or(growthHistory.symptom.containsIgnoreCase(keyword))
            .or(growthHistory.diagnosis.containsIgnoreCase(keyword))
            .or(growthHistory.medicationMethod.containsIgnoreCase(keyword))
            .or(growthHistory.memo.containsIgnoreCase(keyword));
    }

    private BooleanExpression inPetNames(List<String> petNames) {
        return petNames.isEmpty() ? null : pet.name.in(petNames);
    }

    private BooleanExpression inWriterNames(List<String> writerNames) {
        return writerNames.isEmpty() ? null : member.nickname.in(writerNames);
    }
}
