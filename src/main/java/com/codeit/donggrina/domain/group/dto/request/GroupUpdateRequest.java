package com.codeit.donggrina.domain.group.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record GroupUpdateRequest(
    @NotBlank(message = "그룹 이름을 입력해주세요.") String name
) {

}
