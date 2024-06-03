package com.codeit.donggrina.domain.growth_history.repository;

import com.codeit.donggrina.domain.growth_history.entity.GrowthHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrowthHistoryRepository extends JpaRepository<GrowthHistory, Long> {

}
