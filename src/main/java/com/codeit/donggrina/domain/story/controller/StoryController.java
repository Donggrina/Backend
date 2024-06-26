package com.codeit.donggrina.domain.story.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import com.codeit.donggrina.domain.story.dto.response.StoryFindListPage;
import com.codeit.donggrina.domain.story.dto.response.StoryFindListResponse;
import com.codeit.donggrina.domain.story.dto.response.StoryFindResponse;
import com.codeit.donggrina.domain.story.service.StoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/{diaryId}")
    public ApiResponse<StoryFindResponse> findStory(@PathVariable Long diaryId,
        @AuthenticationPrincipal CustomOAuth2User user) {

        return ApiResponse.<StoryFindResponse>builder()
            .code(HttpStatus.OK.value())
            .message("스토리 상세 조회 성공")
            .data(storyService.findStory(diaryId, user.getMemberId()))
            .build();
    }

    @GetMapping
    public ApiResponse<StoryFindListPage> findStories(@AuthenticationPrincipal CustomOAuth2User user, Pageable pageable) {

        return ApiResponse.<StoryFindListPage>builder()
            .code(HttpStatus.OK.value())
            .message("스토리 리스트 조회 성공")
            .data(storyService.findStories(user.getMemberId(), pageable))
            .build();
    }
}
