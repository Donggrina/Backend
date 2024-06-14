package com.codeit.donggrina.domain.story.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record StoryFindListPage(
    List<StoryFindListResponse> response,
    int currentPage,
    boolean hasMore
) {

}
