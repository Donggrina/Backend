package com.codeit.donggrina.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {
    private String role;
    private String name;
    private String username;

    public UserDto(String role, String username) {
        this.role = role;
        this.username = username;
    }
}
