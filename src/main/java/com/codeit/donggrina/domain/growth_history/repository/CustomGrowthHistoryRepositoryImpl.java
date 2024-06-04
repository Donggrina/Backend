package com.codeit.donggrina.domain.growth_history.repository;

import static com.codeit.donggrina.domain.growth_history.entity.QGrowthHistory.growthHistory;
import static com.codeit.donggrina.domain.member.entity.QMember.member;
import static com.codeit.donggrina.domain.pet.entity.QPet.pet;

import com.codeit.donggrina.domain.growth_history.dto.response.GrowthHistoryDetailResponse;
import com.codeit.donggrina.domain.growth_history.dto.response.GrowthHistoryListResponse;
import com.codeit.donggrina.domain.growth_history.entity.GrowthHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomGrowthHistoryRepositoryImpl implements CustomGrowthHistoryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GrowthHistoryListResponse> findGrowthHistoryDetailByDate(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return queryFactory
            .selectFrom(growthHistory)
            .leftJoin(growthHistory.member, member).fetchJoin()
            .leftJoin(member.profileImage).fetchJoin()
            .leftJoin(growthHistory.pet, pet).fetchJoin()
            .leftJoin(pet.profileImage).fetchJoin()
            .where(growthHistory.date.eq(date))
            .orderBy(growthHistory.id.desc())
            .fetch()
            .stream()
            .map(GrowthHistoryListResponse::from)
            .toList();
    }

    @Override
    public GrowthHistoryDetailResponse findGrowthHistoryDetail(Long growthId) {
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
        return GrowthHistoryDetailResponse.from(findGrowthHistory);
    }
}
