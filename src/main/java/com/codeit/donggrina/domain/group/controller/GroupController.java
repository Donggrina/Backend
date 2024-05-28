package com.codeit.donggrina.domain.group.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.group.dto.request.GroupAppendRequest;
import com.codeit.donggrina.domain.group.dto.request.GroupMemberAddRequest;
import com.codeit.donggrina.domain.group.service.GroupService;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

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
            .data(null)
            .build();
    }
}
