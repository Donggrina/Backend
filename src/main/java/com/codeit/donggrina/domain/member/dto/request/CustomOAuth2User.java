package com.codeit.donggrina.domain.member.dto.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final MemberSecurityDto memberSecurityDto;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberSecurityDto.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return memberSecurityDto.getName();
    }

    public String getUserName() {
        return memberSecurityDto.getUsername();
    }

    public Long getMemberId() {
        return memberSecurityDto.getId();
    }

}
