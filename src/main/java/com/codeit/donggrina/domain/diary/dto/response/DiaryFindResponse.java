package com.codeit.donggrina.domain.diary.dto.response;

import com.codeit.donggrina.domain.comment.dto.response.CommentFindResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record DiaryFindResponse(
    LocalDate date,
    String authorImage,
    String author,
    List<Long> petIds,
    List<String> petImages,
    List<Long> contentImageIds,
    List<String> contentImages,
    String content,
    String weather,
    boolean favoriteState,
    int favoriteCount,
    List<CommentFindResponse> comments,
    boolean isShare,
    boolean isMyDiary
) {

}
