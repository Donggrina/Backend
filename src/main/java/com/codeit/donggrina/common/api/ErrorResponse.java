package com.codeit.donggrina.common.api;

import lombok.Builder;

@Builder
public record ErrorResponse(int code, String message) {

}
