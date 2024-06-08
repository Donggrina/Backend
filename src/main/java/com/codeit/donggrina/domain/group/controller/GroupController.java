package com.codeit.donggrina.domain.group.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.group.dto.request.GroupAppendRequest;
import com.codeit.donggrina.domain.group.dto.request.GroupMemberAddRequest;
import com.codeit.donggrina.domain.group.dto.request.GroupUpdateRequest;
import com.codeit.donggrina.domain.group.dto.response.GroupCodeResponse;
import com.codeit.donggrina.domain.group.dto.response.GroupDetailResponse;
import com.codeit.donggrina.domain.group.service.GroupService;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/my/groups")
    public ApiResponse<GroupDetailResponse> getDetail(
        @AuthenticationPrincipal CustomOAuth2User user) {
        Long userId = user.getMemberId();
        GroupDetailResponse result = groupService.getDetail(userId);
        return ApiResponse.<GroupDetailResponse>builder()
            .code(HttpStatus.OK.value())
            .message("가족(그룹) 상세 조회 성공")
            .data(result)
            .build();
    }

    @GetMapping("/my/groups/code")
    public ApiResponse<GroupCodeResponse> getGroupCode(
        @AuthenticationPrincipal CustomOAuth2User member
    ) {
        Long memberId = member.getMemberId();
        return ApiResponse.<GroupCodeResponse>builder()
            .code(HttpStatus.OK.value())
            .message("가족(그룹) 초대코드 조회 성공")
            .data(groupService.getGroupCode(memberId))
            .build();
    }

    @PostMapping("/my/groups")
    public ApiResponse<Long> append(@RequestBody @Validated GroupAppendRequest request,
        @AuthenticationPrincipal CustomOAuth2User user) {
        Long userId = user.getMemberId();
        Long result = groupService.append(request, userId);
        return ApiResponse.<Long>builder()
            .code(HttpStatus.OK.value())
            .message("가족(그룹) 등록 성공")
            .data(result)
            .build();
    }

    @PostMapping("/my/groups/members")
    public ApiResponse<Void> addMember(@RequestBody @Validated GroupMemberAddRequest request,
        @AuthenticationPrincipal CustomOAuth2User user) {
        Long userId = user.getMemberId();
        groupService.addMember(request, userId);
        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("가족(그룹) 멤버 추가 성공")
            .build();
    }

    @PutMapping("/my/groups/{groupId}")
    public ApiResponse<Void> update(
        @PathVariable Long groupId,
        @RequestBody @Validated GroupUpdateRequest request,
        @AuthenticationPrincipal CustomOAuth2User user
    ) {
        Long userId = user.getMemberId();
        groupService.update(groupId, request, userId);
        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("가족(그룹) 정보 수정 성공")
            .build();
    }

    @DeleteMapping("/my/groups/{groupId}")
    public ApiResponse<Void> delete(
        @PathVariable Long groupId,
        @AuthenticationPrincipal CustomOAuth2User user
    ) {
        Long userId = user.getMemberId();
        groupService.delete(groupId, userId);
        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("가족(그룹) 삭제 성공")
            .build();
    }

    @PostMapping("/my/groups/{groupId}/members/{targetId}")
    public ApiResponse<Void> deleteMember(
        @PathVariable Long targetId,
        @PathVariable Long groupId,
        @AuthenticationPrincipal CustomOAuth2User user
    ) {
        Long userId = user.getMemberId();
        groupService.deleteMember(targetId, groupId, userId);
        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("가족(그룹) 멤버 삭제 성공")
            .build();
    }
}
