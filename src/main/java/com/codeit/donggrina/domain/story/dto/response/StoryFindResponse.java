package com.codeit.donggrina.domain.story.dto.response;

import com.codeit.donggrina.domain.comment.dto.response.CommentFindResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record StoryFindResponse(
    String authorImage,
    String authorGroup,
    List<String> images,
    String content,
    LocalDateTime createdDate,
    boolean favoriteState,
    int favoriteCount,
    List<CommentFindResponse> comments
) {

}
