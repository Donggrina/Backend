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
    String content,
    LocalDateTime dateTime,
    String nickname,
    String petName,
    boolean isMine
) {
    public static GrowthHistoryListResponse from(GrowthHistory growthHistory, boolean isMine) {

        String content;
        content = switch (growthHistory.getCategory().getValue()) {
            case "사료" -> growthHistory.getFood();
            case "간식" -> growthHistory.getSnack();
            case "이상 증상" -> growthHistory.getAbnormalSymptom();
            default -> growthHistory.getSymptom();
        };

        return GrowthHistoryListResponse.builder()
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
