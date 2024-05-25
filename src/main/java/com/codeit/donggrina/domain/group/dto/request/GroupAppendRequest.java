package com.codeit.donggrina.domain.group.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record GroupAppendRequest(
    @NotBlank(message = "가족(그룹) 이름 입력을 다시 확인해주세요.") String name,
    @NotBlank(message = "가족(그룹) 생성자 이름 입력을 다시 확인해주세요.") String creatorName
) {

}
