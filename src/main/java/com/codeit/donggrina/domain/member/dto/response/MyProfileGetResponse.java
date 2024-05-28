package com.codeit.donggrina.domain.member.dto.response;

public record MyProfileGetResponse(
    Long id,
    String name,
    String profileImageUrl
) {

}
