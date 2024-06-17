package com.codeit.donggrina.domain.diary.dto.response;

import com.codeit.donggrina.domain.comment.dto.response.CommentFindResponse;
import java.util.List;
import lombok.Builder;

@Builder
public record DiaryFindResponse(
    String authorImage,
    String author,
    List<String> petImages,
    List<String> contentImages,
    String content,
    String weather,
    boolean favoriteState,
    int favoriteCount,
    List<CommentFindResponse> comments,
    boolean isMyDiary
) {

}
