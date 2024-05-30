package com.codeit.donggrina.domain.member.service;

import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import com.codeit.donggrina.domain.member.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String username = customOAuth2User.getUserName();
        Long id = customOAuth2User.getMemberId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        String role = iterator.next().getAuthority();

        String token = jwtUtil.createJwt(id, username, role);
        response.setHeader(HttpHeaders.SET_COOKIE, createCookie("Authorization", token).toString());
        response.sendRedirect("https://www.donggrina.click/start-family");
    }

    private ResponseCookie createCookie(String key, String value) {
        return ResponseCookie.from(key, value)
            .maxAge(60 * 60 * 60 * 60)
            .httpOnly(true)
            .secure(true)
            .domain("donggrina.click")
            .sameSite("None")
            .path("/")
            .build();
    }
}
