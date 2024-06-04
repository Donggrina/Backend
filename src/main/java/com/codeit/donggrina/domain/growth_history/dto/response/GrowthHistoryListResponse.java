package com.codeit.donggrina.domain.growth_history.dto.response;

import com.codeit.donggrina.domain.growth_history.entity.GrowthHistory;
import com.codeit.donggrina.domain.growth_history.entity.GrowthHistoryCategory;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GrowthHistoryListResponse(
    Long id,
    String writerProfileImageUrl,
    String petProfileImageUrl,
    GrowthHistoryCategory category,
    LocalDateTime dateTime,
    String nickname
) {
    public static GrowthHistoryListResponse from(GrowthHistory growthHistory) {
        return GrowthHistoryListResponse.builder()
            .id(growthHistory.getId())
            .writerProfileImageUrl(growthHistory.getMember().getProfileImage().getUrl())
            .petProfileImageUrl(growthHistory.getPet().getProfileImage().getUrl())
            .category(growthHistory.getCategory())
            .dateTime(growthHistory.getCreatedAt())
            .nickname(growthHistory.getMember().getNickname())
            .build();
    }
}
