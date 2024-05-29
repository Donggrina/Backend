package com.codeit.donggrina.domain.group.dto.response;

import com.codeit.donggrina.domain.member.dto.response.MyProfileGetResponse;
import java.util.List;
import lombok.Builder;

@Builder
public record GroupDetailResponse(
    Long id,
    String name,
    String invitationCode,
    List<MyProfileGetResponse> members,
    Long membersCount
) {

}
