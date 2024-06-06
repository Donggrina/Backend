package com.codeit.donggrina.domain.calendar.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.calendar.dto.request.CalendarAppendRequest;
import com.codeit.donggrina.domain.calendar.dto.request.CalendarUpdateRequest;
import com.codeit.donggrina.domain.calendar.service.CalendarService;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @PostMapping("/calendar")
    public ApiResponse<Long> append(
        @RequestBody @Validated CalendarAppendRequest request,
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        return ApiResponse.<Long>builder()
            .code(HttpStatus.OK.value())
            .message("일정 등록 성공")
            .data(calendarService.append(memberId, request))
            .build();
    }

    @PutMapping("/calendar/{calendarId}")
    public ApiResponse<Void> update(
        @PathVariable Long calendarId,
        @RequestBody @Validated CalendarUpdateRequest request,
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        calendarService.update(memberId, calendarId, request);
        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("일정 수정 성공")
            .build();
    }

    @DeleteMapping("/calendar/{calendarId}")
    public ApiResponse<Void> delete(
        @PathVariable Long calendarId,
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        calendarService.delete(memberId, calendarId);
        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("일정 삭제 성공")
            .build();
    }
}
