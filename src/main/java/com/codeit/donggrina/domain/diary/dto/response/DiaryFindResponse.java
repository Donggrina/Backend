package com.codeit.donggrina.domain.diary.dto.response;

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
    boolean isMyDiary
) {

}
