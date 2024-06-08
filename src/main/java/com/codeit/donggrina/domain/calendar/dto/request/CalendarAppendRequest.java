package com.codeit.donggrina.domain.calendar.dto.request;

import com.codeit.donggrina.domain.calendar.entity.CalendarCategory;
import com.codeit.donggrina.domain.calendar.util.IsoDateTime;
import jakarta.validation.constraints.NotBlank;

public record CalendarAppendRequest(
    @NotBlank(message = "제목을 입력해주세요.")
    String title,
    @NotBlank(message = "내용을 입력해주세요.")
    String memo,
    @NotBlank(message = "반려동물을 선택해주세요.")
    String petName,
    CalendarCategory category,
    @IsoDateTime
    String dateTime
) {

}
