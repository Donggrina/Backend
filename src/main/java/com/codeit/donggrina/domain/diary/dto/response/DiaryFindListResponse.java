package com.codeit.donggrina.domain.diary.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record DiaryFindListResponse(
    Long diaryId,
    String authorImage,
    String author,
    List<String> petImages,
    String content,
    String contentImage,
//    int commentCount,
//    int favoriteCount,
//    boolean favoriteState,
    boolean isMyDiary
) {

}
