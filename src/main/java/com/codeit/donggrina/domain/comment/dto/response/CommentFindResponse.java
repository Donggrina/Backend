package com.codeit.donggrina.domain.comment.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record CommentFindResponse(
    Long commentId,
    String commentAuthorImage,
    String commentAuthor,
    String comment,
    LocalDate date,
    boolean isMyComment,
    List<CommentFindResponse> children
) {

}
