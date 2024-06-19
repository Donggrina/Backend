package com.codeit.donggrina.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSecurityDto {
    private Long id;
    private String role;
    private String name;
    private String username;
    private boolean isFamily;

    public MemberSecurityDto(Long id, String role, String username, boolean isFamily) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.isFamily = isFamily;
    }
}
