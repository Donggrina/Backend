package com.codeit.donggrina.domain.comment.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.comment.dto.request.CommentAppendRequest;
import com.codeit.donggrina.domain.comment.dto.request.CommentUpdateRequest;
import com.codeit.donggrina.domain.comment.service.CommentService;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/{diaryId}")
    public ApiResponse<Long> append(
        @PathVariable Long diaryId,
        @RequestBody @Validated CommentAppendRequest request,
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        return ApiResponse.<Long>builder()
            .code(HttpStatus.OK.value())
            .message("댓글 작성 성공")
            .data(commentService.append(diaryId, request, memberId))
            .build();
    }

    @PutMapping("/comments/{commentId}")
    public ApiResponse<Void> update(
        @PathVariable Long commentId,
        @RequestBody @Validated CommentUpdateRequest request,
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        commentService.update(commentId, request, memberId);
        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("댓글 수정 성공")
            .build();
    }
}
