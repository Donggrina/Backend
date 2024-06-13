package com.codeit.donggrina.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentAppendRequest(
    @NotBlank(message = "내용을 입력해주세요.")
    String content,
    Long parentCommentId
) {

}
