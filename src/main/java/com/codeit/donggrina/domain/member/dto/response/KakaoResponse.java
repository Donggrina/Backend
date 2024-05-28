package com.codeit.donggrina.domain.member.dto.response;

import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoResponse {

    private final Map<String, Object> attribute;

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
}
