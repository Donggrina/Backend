package com.codeit.donggrina.domain.group.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record GroupMemberAddRequest(
    @NotBlank(message = "초대 코드 입력을 다시 확인해주세요.") String code
) {

}
