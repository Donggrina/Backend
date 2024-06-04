package com.codeit.donggrina.domain.growth_history.dto;

import lombok.Builder;

@Builder
public record GrowthHistoryContentDto(
    String food,
    String snack,
    String abnormalSymptom,
    String hospitalName,
    String symptom,
    String diagnosis,
    String medicationMethod,
    Integer price,
    String memo
) {
}
