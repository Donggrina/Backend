package com.codeit.donggrina.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberUpdateRequest(
    Long imageId,
    @NotBlank(message = "이름을 입력해주세요")
    String nickname
) {

}
