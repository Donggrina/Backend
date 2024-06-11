package com.codeit.donggrina.domain.growth_history.repository;

import com.codeit.donggrina.common.api.SearchFilter;
import com.codeit.donggrina.domain.growth_history.entity.GrowthHistory;
import java.time.LocalDate;
import java.util.List;

public interface CustomGrowthHistoryRepository {

    List<GrowthHistory> findGrowthHistoryDetailByDate(Long groupId, LocalDate date);

    GrowthHistory findGrowthHistoryDetail(Long growthId);

    List<GrowthHistory> findGrowthHistoryBySearchFilter(SearchFilter searchFilter);
}
