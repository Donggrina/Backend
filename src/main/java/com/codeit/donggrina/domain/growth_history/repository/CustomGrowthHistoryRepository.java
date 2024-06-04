package com.codeit.donggrina.domain.growth_history.repository;

import com.codeit.donggrina.domain.growth_history.dto.response.GrowthHistoryDetailResponse;

public interface CustomGrowthHistoryRepository {

    GrowthHistoryDetailResponse findGrowthHistoryDetail(Long growthId);
}
