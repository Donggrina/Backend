package com.codeit.donggrina.domain.member.dto.response;

import java.util.Map;

public class GoogleResponse implements OAuth2Response{

    private final String DEFAULT_IMAGE_URL;
    private final Map<String, Object> attribute;

    public GoogleResponse(String DEFAULT_IMAGE_URL, Map<String, Object> attribute) {
        this.DEFAULT_IMAGE_URL = DEFAULT_IMAGE_URL;
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }

    @Override
    public String getProfileImageUrl() {
        return DEFAULT_IMAGE_URL;
    }
}
