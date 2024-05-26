package com.codeit.donggrina.common.config;

import com.codeit.donggrina.domain.member.jwt.JwtFilter;
import com.codeit.donggrina.domain.member.jwt.JwtUtil;
import com.codeit.donggrina.domain.member.service.CustomOAuth2UserService;
import com.codeit.donggrina.domain.member.service.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((request) -> request
                .requestMatchers("/members/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
            .oauth2Login((oauth2) -> oauth2
                .redirectionEndpoint(endpoint -> endpoint.baseUri("/members/login/kakao"))
                .userInfoEndpoint((userInfoEndpointConfig) ->
                    userInfoEndpointConfig.userService(customOAuth2UserService))
                .successHandler(oAuth2LoginSuccessHandler)
            );

        return httpSecurity.build();
    }

}
