package com.codeit.donggrina.domain.member.dto.response;

import java.util.Map;
import lombok.RequiredArgsConstructor;

public class KakaoResponse {

    private final String DEFAULT_IMAGE_URL;
    private final Map<String, Object> attribute;

    public KakaoResponse(String DEFAULT_IMAGE_URL, Map<String, Object> attribute) {
        this.DEFAULT_IMAGE_URL = DEFAULT_IMAGE_URL;
        this.attribute = attribute;
    }

    public String getProvider() {
        return "kakao";
    }
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    public String getName() {
        Map<String, String> properties = (Map<String, String>) attribute.get("properties");
        return properties.get("nickname");
    }

    public String getProfileImageUrl(){
        Map<String, String> properties = (Map<String, String>) attribute.get("properties");
        return properties.getOrDefault("profile_image_url", DEFAULT_IMAGE_URL);
    }

}
