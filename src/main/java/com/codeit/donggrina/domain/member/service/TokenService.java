package com.codeit.donggrina.domain.member.service;

import com.codeit.donggrina.domain.member.dto.request.RefreshTokenRequest;
import com.codeit.donggrina.domain.member.jwt.JwtUtil;
import com.codeit.donggrina.domain.member.repository.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final long ACCESS_EXPIRED_MS = 1000 * 60 * 60 * 3;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final JwtUtil jwtUtil;

    public String getAccessToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.refreshToken();
        if(refreshTokenRedisRepository.hasRefreshToken(refreshToken)) {
            Long memberId = jwtUtil.getMemberId(refreshToken);
            String username = jwtUtil.getUsername(refreshToken);
            String role = jwtUtil.getRole(refreshToken);

        return jwtUtil.createJwt(memberId, username, role, ACCESS_EXPIRED_MS);
        }
        throw new IllegalArgumentException("로그인이 만료되었습니다.");
    }
}
