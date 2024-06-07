package com.codeit.donggrina.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
    @NotBlank(message = "access 토큰을 입력하세요.")
    String accessToken,
    @NotBlank(message = "refresh 토큰을 입력하세요.")
    String refreshToken
) {

}
