package com.codeit.donggrina.domain.growth_history.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.growth_history.dto.request.GrowthHistoryAppendRequest;
import com.codeit.donggrina.domain.growth_history.dto.request.GrowthHistoryUpdateRequest;
import com.codeit.donggrina.domain.growth_history.dto.request.SearchFilter;
import com.codeit.donggrina.domain.growth_history.dto.response.GrowthHistoryDetailResponse;
import com.codeit.donggrina.domain.growth_history.dto.response.GrowthHistoryListResponse;
import com.codeit.donggrina.domain.growth_history.service.GrowthHistoryService;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import jakarta.annotation.Nullable;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class GrowthHistoryController {

    private final GrowthHistoryService growthHistoryService;

    @GetMapping("/growth")
    public ApiResponse<List<GrowthHistoryListResponse>> getByDate(
        @RequestParam @Nullable LocalDate date
    ) {
        return ApiResponse.<List<GrowthHistoryListResponse>>builder()
            .code(HttpStatus.OK.value())
            .message("성장기록 날짜별 조회 성공")
            .data(growthHistoryService.getByDate(date))
            .build();
    }

    @GetMapping("/growth/{growthId}")
    public ApiResponse<GrowthHistoryDetailResponse> getDetail(
        @PathVariable Long growthId
    ) {
        return ApiResponse.<GrowthHistoryDetailResponse>builder()
            .code(HttpStatus.OK.value())
            .message("성장기록 상세 조회 성공")
            .data(growthHistoryService.getDetail(growthId))
            .build();
    }

    @GetMapping("/growth/search")
    public ApiResponse<List<GrowthHistoryListResponse>> search(
        SearchFilter searchFilter
    ) {
        return ApiResponse.<List<GrowthHistoryListResponse>>builder()
            .code(HttpStatus.OK.value())
            .message("성장기록 검색 성공")
            .data(growthHistoryService.search(searchFilter))
            .build();
    }

    @PostMapping("/growth")
    public ApiResponse<Long> append(
        @RequestBody GrowthHistoryAppendRequest request,
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        Long result = growthHistoryService.append(memberId, request);
        return ApiResponse.<Long>builder()
            .code(HttpStatus.OK.value())
            .message("성장기록 추가 성공")
            .data(result)
            .build();
    }

    @PutMapping("/growth/{growthId}")
    public ApiResponse<Void> update(
        @PathVariable Long growthId,
        @RequestBody GrowthHistoryUpdateRequest request,
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        growthHistoryService.update(memberId, growthId, request);
        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("성장기록 수정 성공")
            .build();
    }

    @DeleteMapping("/growth/{growthId}")
    public ApiResponse<Void> delete(
        @PathVariable Long growthId,
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        growthHistoryService.delete(memberId, growthId);
        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("성장기록 삭제 성공")
            .build();
    }
}
