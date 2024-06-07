package com.codeit.donggrina.domain.calendar.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.calendar.dto.request.CalendarAppendRequest;
import com.codeit.donggrina.domain.calendar.dto.request.CalendarUpdateRequest;
import com.codeit.donggrina.domain.calendar.dto.response.CalendarDailyCountResponse;
import com.codeit.donggrina.domain.calendar.dto.response.CalendarDetailResponse;
import com.codeit.donggrina.domain.calendar.dto.response.CalendarListResponse;
import com.codeit.donggrina.domain.calendar.service.CalendarService;
import com.codeit.donggrina.common.api.SearchFilter;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import jakarta.annotation.Nullable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/calendar/month")
    public ApiResponse<List<CalendarDailyCountResponse>> getDailyCountByMonth(
        @RequestParam @Nullable YearMonth yearMonth,
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        return ApiResponse.<List<CalendarDailyCountResponse>>builder()
            .code(HttpStatus.OK.value())
            .message("일정 목록 월별 조회 성공")
            .data(calendarService.getDailyCountByMonth(memberId, yearMonth))
            .build();
    }

    @GetMapping("/calendar/day")
    public ApiResponse<List<CalendarListResponse>> getDayListByDate(
        @RequestParam @Nullable LocalDate date,
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        return ApiResponse.<List<CalendarListResponse>>builder()
            .code(HttpStatus.OK.value())
            .message("일정 목록 일별 조회 성공")
            .data(calendarService.getDayListByDate(memberId, date))
            .build();
    }

    @GetMapping("/calendar/{calendarId}")
    public ApiResponse<CalendarDetailResponse> getDetail(
        @PathVariable Long calendarId
    ) {
        return ApiResponse.<CalendarDetailResponse>builder()
            .code(HttpStatus.OK.value())
            .message("일정 상세 조회 성공")
            .data(calendarService.getDetail(calendarId))
            .build();
    }

    @GetMapping("/calendar/search")
    public ApiResponse<List<CalendarListResponse>> search(SearchFilter searchFilter) {
        return ApiResponse.<List<CalendarListResponse>>builder()
            .code(HttpStatus.OK.value())
            .message("일정 검색 성공")
            .data(calendarService.search(searchFilter))
            .build();
    }

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

    @PutMapping("/calendar/completion/{calendarId}")
    public ApiResponse<Void> updateCompletionState(
        @PathVariable Long calendarId,
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        calendarService.updateCompletionState(memberId, calendarId);
        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("일정 완료 여부 변경 성공")
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
