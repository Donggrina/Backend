package com.codeit.donggrina.domain.calendar.dto.response;

import com.codeit.donggrina.domain.calendar.entity.Calendar;
import com.codeit.donggrina.domain.calendar.entity.CalendarCategory;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CalendarListResponse(
    Long id,
    String title,
    CalendarCategory category,
    LocalDateTime dateTime,
    String memberProfileImageUrl,
    String nickname,
    String petProfileImageUrl
) {

    public static CalendarListResponse from(Calendar calendar) {
        return CalendarListResponse.builder()
            .id(calendar.getId())
            .title(calendar.getTitle())
            .category(calendar.getCategory())
            .dateTime(calendar.getDateTime())
            .memberProfileImageUrl(calendar.getMember().getProfileImage().getUrl())
            .nickname(calendar.getMember().getNickname())
            .petProfileImageUrl(calendar.getPet().getProfileImage().getUrl())
            .build();
    }

}
