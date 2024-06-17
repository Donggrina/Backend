package com.codeit.donggrina.domain.growth_history.dto.response;

import com.codeit.donggrina.domain.growth_history.dto.GrowthHistoryContentDto;
import com.codeit.donggrina.domain.growth_history.entity.GrowthHistory;
import com.codeit.donggrina.domain.growth_history.entity.GrowthHistoryCategory;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GrowthHistoryDetailResponse(
    Long id,
    String writerProfileImageUrl,
    String petProfileImageUrl,
    GrowthHistoryCategory category,
    GrowthHistoryContentDto content,
    LocalDateTime dateTime,
    String nickname,
    String petName,
    boolean isMine
) {
    public static GrowthHistoryDetailResponse from(GrowthHistory growthHistory, boolean isMine) {
        GrowthHistoryContentDto content = GrowthHistoryContentDto.builder()
            .food(growthHistory.getFood())
            .snack(growthHistory.getSnack())
            .abnormalSymptom(growthHistory.getAbnormalSymptom())
            .hospitalName(growthHistory.getHospitalName())
            .symptom(growthHistory.getSymptom())
            .diagnosis(growthHistory.getDiagnosis())
            .medicationMethod(growthHistory.getMedicationMethod())
            .price(growthHistory.getPrice())
            .memo(growthHistory.getMemo())
            .build();
        return GrowthHistoryDetailResponse.builder()
            .id(growthHistory.getId())
            .writerProfileImageUrl(growthHistory.getMember().getProfileImage().getUrl())
            .petProfileImageUrl(growthHistory.getPet().getProfileImage().getUrl())
            .category(growthHistory.getCategory())
            .content(content)
            .dateTime(growthHistory.getCreatedAt())
            .nickname(growthHistory.getMember().getNickname())
            .petName(growthHistory.getPet().getName())
            .isMine(isMine)
            .build();
    }
}
