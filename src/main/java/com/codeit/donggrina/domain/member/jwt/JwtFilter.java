package com.codeit.donggrina.domain.member.jwt;

import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import com.codeit.donggrina.domain.member.dto.request.MemberSecurityDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.security.sasl.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException, RuntimeException, IllegalArgumentException {

        String token = null;
//        if(request.getCookies() != null) {
//            for (Cookie cookie : request.getCookies()) {
//                if (cookie.getName().equals("Authorization")) {
//                    token = cookie.getValue();
//                }
//            }
//        }
        if (request.getHeader("Authorization") != null) {
            token = request.getHeader("Authorization").substring(7);
        }
        if (token == null) {
            request.setAttribute("exceptionCode", HttpStatus.BAD_REQUEST.value());
            filterChain.doFilter(request, response);
            return;
        }
        if (jwtUtil.isExpired(token)) {
            request.setAttribute("exceptionCode", HttpStatus.UNAUTHORIZED.value());
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
