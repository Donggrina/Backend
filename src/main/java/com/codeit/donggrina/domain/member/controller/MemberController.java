package com.codeit.donggrina.domain.member.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import com.codeit.donggrina.domain.member.dto.response.MyProfileGetResponse;
import com.codeit.donggrina.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my/profiles")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ApiResponse<MyProfileGetResponse> getMyProfile(@PathVariable("memberId") Long memberId) {
        return ApiResponse.<MyProfileGetResponse>builder()
            .code(HttpStatus.OK.value())
            .message("프로필 조회 성공")
            .data(memberService.getMyProfile(memberId))
            .build();
    }

    @PutMapping("/memberId")
    public ApiResponse<Void> updateMyProfile(@AuthenticationPrincipal CustomOAuth2User user,
        @RequestParam Long imageId, @RequestParam String name) {
        memberService.updateMyProfile(user.getMemberId(), imageId, name);
        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("프로필 수정 성공")
            .build();
    }
}
