package com.codeit.donggrina.domain.member.service;

import com.codeit.donggrina.domain.member.dto.request.RefreshTokenRequest;
import com.codeit.donggrina.domain.member.jwt.JwtUtil;
import com.codeit.donggrina.domain.member.repository.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final long REFRESH_EXPIRED_MS = 1000 * 60 * 60 * 24 * 7;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final JwtUtil jwtUtil;

    public String getAccessToken(RefreshTokenRequest refreshTokenRequest) {
        String accessToken = refreshTokenRequest.accessToken();
        if(refreshTokenRedisRepository.hasRefreshToken(refreshTokenRequest.refreshToken())) {
            Long memberId = jwtUtil.getMemberId(accessToken);
            String username = jwtUtil.getUsername(accessToken);
            String role = jwtUtil.getRole(accessToken);
            return jwtUtil.createJwt(memberId, username, role, REFRESH_EXPIRED_MS);
        }
        throw new IllegalArgumentException("로그인이 만료되었습니다.");
    }
}
