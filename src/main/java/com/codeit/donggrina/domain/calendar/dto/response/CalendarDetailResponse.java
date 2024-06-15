package com.codeit.donggrina.domain.calendar.dto.response;

import com.codeit.donggrina.domain.calendar.entity.Calendar;
import com.codeit.donggrina.domain.calendar.entity.CalendarCategory;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CalendarDetailResponse(
    Long id,
    String title,
    String memo,
    CalendarCategory category,
    LocalDateTime dateTime,
    String writerProfileImageUrl,
    String writerNickName,
    String petProfileImageUrl,
    String petName,
    boolean isFinished,
    boolean isMine
) {

    public static CalendarDetailResponse from(Calendar calendar, boolean isMine) {
        return CalendarDetailResponse.builder()
            .id(calendar.getId())
            .title(calendar.getTitle())
            .memo(calendar.getMemo())
            .category(calendar.getCategory())
            .dateTime(calendar.getDateTime())
            .writerProfileImageUrl(calendar.getMember().getProfileImage().getUrl())
            .writerNickName(calendar.getMember().getNickname())
            .petProfileImageUrl(calendar.getPet().getProfileImage().getUrl())
            .petName(calendar.getPet().getName())
            .isFinished(calendar.isFinished())
            .isMine(isMine)
            .build();
    }
}
