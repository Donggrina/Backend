package com.codeit.donggrina.domain.member.service;

import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import com.codeit.donggrina.domain.member.dto.response.KakaoResponse;
import com.codeit.donggrina.domain.member.dto.request.MemberSecurityDto;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        KakaoResponse kakaoResponse = null;
        if(registrationId.equals("kakao")) {
            kakaoResponse = new KakaoResponse(oAuth2User.getAttributes());
        }else {
            return null;
        }

        String username = kakaoResponse.getProvider() + " " + kakaoResponse.getProviderId();

        Optional<Member> foundMemberOptional = memberRepository.findByUsername(username);
        MemberSecurityDto memberSecurityDto = null;
        if(foundMemberOptional.isEmpty()) {
            Member member = Member.builder()
                .username(username)
                .name(kakaoResponse.getName())
                .role("ROLE_USER")
                .build();

            Member savedMember = memberRepository.save(member);

            memberSecurityDto = new MemberSecurityDto(savedMember.getId(), savedMember.getRole(),
                savedMember.getName(), savedMember.getUsername());
        } else {
            Member foundMember = foundMemberOptional.get();
            memberSecurityDto = new MemberSecurityDto(foundMember.getId(), foundMember.getRole(),
                foundMember.getName(), foundMember.getUsername());
        }

        return new CustomOAuth2User(memberSecurityDto);
    }
}
