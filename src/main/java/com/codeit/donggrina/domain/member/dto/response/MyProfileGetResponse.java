package com.codeit.donggrina.domain.member.dto.response;

import com.codeit.donggrina.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record MyProfileGetResponse(
    Long id,
    String name,
    String nickname,
    Long imageId,
    String profileImageUrl
) {
    public static MyProfileGetResponse from(Member member) {
        return MyProfileGetResponse.builder()
            .id(member.getId())
            .name(member.getName())
            .nickname(member.getNickname())
            .imageId(member.getProfileImage().getId())
            .profileImageUrl(member.getProfileImage().getUrl())
            .build();
    }
}
