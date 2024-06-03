package com.codeit.donggrina.domain.growth_history.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.growth_history.dto.request.GrowthHistoryAppendRequest;
import com.codeit.donggrina.domain.growth_history.service.GrowthHistoryService;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GrowthHistoryController {

    private final GrowthHistoryService growthHistoryService;

    @PostMapping("/growth")
    public ApiResponse<Long> append(
        @RequestBody GrowthHistoryAppendRequest request,
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        Long result = growthHistoryService.append(memberId, request);
        return ApiResponse.<Long>builder()
            .code(200)
            .message("성장기록 추가 성공")
            .data(result)
            .build();
    }
}
