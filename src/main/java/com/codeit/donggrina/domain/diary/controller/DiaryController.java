package com.codeit.donggrina.domain.diary.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.diary.dto.request.DiaryCreateRequest;
import com.codeit.donggrina.domain.diary.dto.request.DiarySearchRequest;
import com.codeit.donggrina.domain.diary.dto.request.DiaryUpdateRequest;
import com.codeit.donggrina.domain.diary.dto.response.DiaryFindListResponse;
import com.codeit.donggrina.domain.diary.dto.response.DiaryFindResponse;
import com.codeit.donggrina.domain.diary.service.DiaryService;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ApiResponse<Void> createDiary(
        @RequestBody @Validated DiaryCreateRequest diaryCreateRequest,
        @AuthenticationPrincipal CustomOAuth2User user) {

        diaryService.createDiary(diaryCreateRequest, user.getMemberId());
        return ApiResponse.<Void>builder()
            .code(HttpStatus.CREATED.value())
            .message("다이어리 등록 성공")
            .build();
    }

    @PutMapping("/{diaryId}")
    public ApiResponse<Void> updateDiary(
        @PathVariable Long diaryId,
        @RequestBody @Validated DiaryUpdateRequest diaryUpdateRequest,
        @AuthenticationPrincipal CustomOAuth2User user) {

        diaryService.updateDiary(diaryId, diaryUpdateRequest, user.getMemberId());

        return ApiResponse.<Void>builder()
            .code(HttpStatus.NO_CONTENT.value())
            .message("다이어리 수정 성공")
            .build();
    }

    @DeleteMapping("/{diaryId}")
    public ApiResponse<Void> deleteDiary(@PathVariable Long diaryId,
        @AuthenticationPrincipal CustomOAuth2User user) {

        diaryService.deleteDiary(diaryId, user.getMemberId());

        return ApiResponse.<Void>builder()
            .code(HttpStatus.NO_CONTENT.value())
            .message("다이어리 삭제 성공")
            .build();
    }

    @GetMapping
    public ApiResponse<List<DiaryFindListResponse>> findDiaries(
        @AuthenticationPrincipal CustomOAuth2User user, @RequestParam(required = false) LocalDate date) {

        if(date == null) {
            date = LocalDate.now();
        }

        return ApiResponse.<List<DiaryFindListResponse>>builder()
            .code(HttpStatus.OK.value())
            .message("다이어리 전체 조회 성공")
            .data(diaryService.findDiaries(user.getMemberId(), date))
            .build();
    }

    @GetMapping("/{diaryId}")
    public ApiResponse<DiaryFindResponse> findDiary(@PathVariable Long diaryId,
        @AuthenticationPrincipal CustomOAuth2User user) {

        return ApiResponse.<DiaryFindResponse>builder()
            .code(HttpStatus.CREATED.value())
            .message("다이어리 상세 조회 성공")
            .data(diaryService.findDiary(diaryId, user.getMemberId()))
            .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<DiaryFindListResponse>> searchDiaries(
        @Validated DiarySearchRequest diarySearchRequest,
        @AuthenticationPrincipal CustomOAuth2User user) {
        
        return ApiResponse.<List<DiaryFindListResponse>>builder()
            .code(HttpStatus.OK.value())
            .message("다이어리 검색 성공")
            .data(diaryService.searchDiaries(diarySearchRequest, user.getMemberId()))
            .build();

    }

}
