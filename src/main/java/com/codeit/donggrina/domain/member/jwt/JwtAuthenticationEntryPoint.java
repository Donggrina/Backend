package com.codeit.donggrina.domain.member.jwt;


import com.codeit.donggrina.common.api.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        if(request.getAttribute("exceptionCode") == null) {
            return;
        }

        int exceptionCode = (Integer) request.getAttribute("exceptionCode");

        if(exceptionCode == HttpStatus.UNAUTHORIZED.value()) {
            sendResponse(response, exceptionCode, "토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED.value());
        } else if(exceptionCode == HttpStatus.BAD_REQUEST.value()) {
            sendResponse(response, exceptionCode, "토큰이 존재하지않습니다.", HttpStatus.BAD_REQUEST.value());
        }
    }

    private void sendResponse(HttpServletResponse response, int exceptionCode, String message, int status) {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(exceptionCode)
            .message(message)
            .build();
        try {
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().println(jsonResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
