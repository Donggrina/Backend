package com.codeit.donggrina.domain.member.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.member.dto.request.RefreshTokenRequest;
import com.codeit.donggrina.domain.member.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ApiResponse<String> reAuthenticate(
        @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ApiResponse.<String>builder()
            .code(HttpStatus.CREATED.value())
            .message("토큰 재발급 성공")
            .data(tokenService.getAccessToken(refreshTokenRequest))
            .build();
    }

    @GetMapping("/test")
    public ApiResponse<String> test() {
        return ApiResponse.<String>builder()
            .code(HttpStatus.CREATED.value())
            .message("토큰 재발급 성공")
            .data("tester")
            .build();    }
}
