package com.codeit.donggrina.common.api;

import lombok.Builder;

@Builder
public record ApiResponse<T>(
    int code,
    String message,
    T data
) {

  public static <T> ApiResponse<T> of(int code, String message, T data) {
    return new ApiResponse<>(code, message, data);
  }
}
