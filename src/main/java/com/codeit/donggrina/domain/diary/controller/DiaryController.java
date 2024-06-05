package com.codeit.donggrina.domain.diary.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.diary.dto.request.DiaryCreateRequest;
import com.codeit.donggrina.domain.diary.service.DiaryService;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
