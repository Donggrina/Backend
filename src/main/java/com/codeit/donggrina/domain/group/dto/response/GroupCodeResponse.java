package com.codeit.donggrina.domain.group.dto.response;

import lombok.Builder;

@Builder
public record GroupCodeResponse(
    Long id,
    String code
) {

}
