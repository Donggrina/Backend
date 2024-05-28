package com.codeit.donggrina.domain.member.jwt;

import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import com.codeit.donggrina.domain.member.dto.request.MemberSecurityDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) {
                token = cookie.getValue();
            }
        }

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtUtil.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Long id = jwtUtil.getMemberId(token);
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(new MemberSecurityDto(id, role, username));

        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
            customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
