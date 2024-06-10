package com.codeit.donggrina.domain.heart.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.heart.service.HeartService;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/hearts/{diaryId}")
    public ApiResponse<Void> append(
        @PathVariable Long diaryId,
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        heartService.append(memberId, diaryId);
        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("좋아요 성공.")
            .build();
    }

    @DeleteMapping("/hearts/{diaryId}")
    public ApiResponse<Void> cancel(
        @PathVariable Long diaryId,
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        heartService.cancel(memberId, diaryId);
        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("좋아요 취소 성공.")
            .build();
    }
}
