package com.codeit.donggrina.common.config;

import com.codeit.donggrina.domain.member.jwt.JwtAuthenticationEntryPoint;
import com.codeit.donggrina.domain.member.jwt.JwtFilter;
import com.codeit.donggrina.domain.member.jwt.JwtUtil;
import com.codeit.donggrina.domain.member.service.CustomOAuth2UserService;
import com.codeit.donggrina.domain.member.service.OAuth2LoginSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(
                    Collections.singletonList("https://www.donggrina.click"));
                configuration.setAllowedMethods(
                    Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowCredentials(true);
                configuration.setMaxAge(3600L);
                configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
                return configuration;
            }))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests((request) -> request
                .requestMatchers("/members/**", "/", "/health", "/refresh").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(AbstractHttpConfigurer::disable)
            .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(e -> e
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint(objectMapper))
            )
            .oauth2Login((oauth2) -> oauth2
                .redirectionEndpoint(endpoint -> endpoint.baseUri("/members/login/kakao"))
                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                    .userService(customOAuth2UserService))
                .successHandler(oAuth2LoginSuccessHandler)
            );

        return httpSecurity.build();
    }

}
