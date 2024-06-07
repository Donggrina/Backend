package com.codeit.donggrina.domain.growth_history.repository;

import com.codeit.donggrina.domain.growth_history.dto.request.SearchFilter;
import com.codeit.donggrina.domain.growth_history.dto.response.GrowthHistoryDetailResponse;
import com.codeit.donggrina.domain.growth_history.dto.response.GrowthHistoryListResponse;
import java.time.LocalDate;
import java.util.List;

public interface CustomGrowthHistoryRepository {

    List<GrowthHistoryListResponse> findGrowthHistoryDetailByDate(Long groupId, LocalDate date);

    GrowthHistoryDetailResponse findGrowthHistoryDetail(Long growthId);

    List<GrowthHistoryListResponse> findGrowthHistoryBySearchFilter(SearchFilter searchFilter);
}
