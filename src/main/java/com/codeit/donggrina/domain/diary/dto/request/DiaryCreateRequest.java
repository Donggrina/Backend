package com.codeit.donggrina.domain.diary.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record DiaryCreateRequest(
    @NotNull(message = "반려동물을 선택해주세요.")
    List<Long> pets,
    List<Long> images,
    @NotNull(message = "내용을 입력해주세요")
    String content,
    @NotNull(message = "날씨를 입력해주세요")
    String weather,
    @NotNull(message = "공유 여부를 선택해주세요")
    Boolean isShare,
    @NotNull(message = "날짜를 입력해주세요")
    LocalDate date
) {

}
