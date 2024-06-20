package com.codeit.donggrina.domain.story.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record StoryFindListResponse(
    Long diaryId,
    String authorImage,
    String author,
    List<String> petImages,
    String weather,
    String authorGroup,
    List<String> images,
    String content,
    int commentCount,
    int favoriteCount,
    boolean favoriteState,
    LocalDate date,
    boolean isMyStory
) {

}
