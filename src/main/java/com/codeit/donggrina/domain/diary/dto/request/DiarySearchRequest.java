package com.codeit.donggrina.domain.diary.dto.request;

import java.util.List;

public record DiarySearchRequest(
    List<Long> petIds,
    List<String> authors,
    String keyword
) {

}
