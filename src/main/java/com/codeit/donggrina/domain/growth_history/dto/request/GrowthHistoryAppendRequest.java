package com.codeit.donggrina.domain.growth_history.dto.request;

import com.codeit.donggrina.domain.growth_history.entity.GrowthHistoryCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record GrowthHistoryAppendRequest(
    @NotNull(message = "날짜를 입력해주세요.")
    LocalDate date,
    @NotBlank(message = "반려동물을 선택해주세요.")
    String petName,
    @NotNull(message = "카테고리를 입력해주세요.")
    GrowthHistoryCategory category,
    GrowthHistoryContentDto content
) {

}
