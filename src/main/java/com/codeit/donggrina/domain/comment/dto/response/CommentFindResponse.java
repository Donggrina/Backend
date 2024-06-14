package com.codeit.donggrina.domain.comment.dto.response;

import lombok.Builder;

@Builder
public record CommentFindResponse(
    Long commentId,
    String commentAuthorImage,
    String commentAuthor,
    String comment
) {

}
