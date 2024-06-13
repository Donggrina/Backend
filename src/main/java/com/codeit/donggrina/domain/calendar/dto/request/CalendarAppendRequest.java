package com.codeit.donggrina.domain.calendar.dto.request;

import com.codeit.donggrina.domain.calendar.entity.CalendarCategory;
import com.codeit.donggrina.domain.calendar.util.IsoDateTime;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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

    public LocalDateTime getLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        return localDateTime.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }
}
