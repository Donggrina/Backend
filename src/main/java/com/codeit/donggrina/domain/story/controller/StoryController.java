package com.codeit.donggrina.domain.story.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import com.codeit.donggrina.domain.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
public class StoryController {

    private final StoryService storyService;

    @PostMapping("/{diaryId}")
    public ApiResponse<Void> createStory(@PathVariable Long diaryId, @AuthenticationPrincipal
        CustomOAuth2User user) {

        storyService.createStory(diaryId, user.getMemberId());
        return ApiResponse.<Void>builder()
            .code(HttpStatus.CREATED.value())
            .message("스토리 등록 성공")
            .build();
    }

    @DeleteMapping("/{diaryId}")
    public ApiResponse<Void> deleteStory(@PathVariable Long diaryId,
        @AuthenticationPrincipal CustomOAuth2User user) {

        storyService.deleteStory(diaryId, user.getMemberId());
        return ApiResponse.<Void>builder()
            .code(HttpStatus.NO_CONTENT.value())
            .message("스토리 삭제 성공")
            .build();
    }
}
