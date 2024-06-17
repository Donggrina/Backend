package com.codeit.donggrina.domain.member.dto.response;

public interface OAuth2Response {
    String getProvider();
    String getProviderId();
    String getName();
    String getProfileImageUrl();
}
