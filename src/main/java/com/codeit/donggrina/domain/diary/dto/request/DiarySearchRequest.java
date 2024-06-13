package com.codeit.donggrina.domain.diary.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record DiarySearchRequest(
    @NotNull(message = "날짜를 입력해주세요")
    LocalDate date,
    List<Long> petIds,
    List<String> authors,
    String keyword
) {

}
