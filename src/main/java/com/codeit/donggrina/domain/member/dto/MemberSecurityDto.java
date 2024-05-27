package com.codeit.donggrina.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSecurityDto {
    private Long id;
    private String role;
    private String name;
    private String username;

    public MemberSecurityDto(Long id, String role, String username) {
        this.id = id;
        this.role = role;
        this.username = username;
    }
}
