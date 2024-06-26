package com.codeit.donggrina.domain.member.service;

import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import com.codeit.donggrina.domain.ProfileImage.repository.ProfileImageRepository;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import com.codeit.donggrina.domain.member.dto.response.GoogleResponse;
import com.codeit.donggrina.domain.member.dto.response.KakaoResponse;
import com.codeit.donggrina.domain.member.dto.request.MemberSecurityDto;
import com.codeit.donggrina.domain.member.dto.response.OAuth2Response;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Value("${image.url.default.member}")
    private  String DEFAULT_IMAGE_URL;
    private final MemberRepository memberRepository;
    private final ProfileImageRepository profileImageRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if(registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(DEFAULT_IMAGE_URL, oAuth2User.getAttributes());
        }else {
            oAuth2Response = new GoogleResponse(DEFAULT_IMAGE_URL, oAuth2User.getAttributes());;
        }

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        Optional<Member> foundMemberOptional = memberRepository.findByUsername(username);
        MemberSecurityDto memberSecurityDto = null;
        if(foundMemberOptional.isEmpty()) {
            ProfileImage profileImage = ProfileImage.builder()
                    .name("member_profile_image")
                    .url(oAuth2Response.getProfileImageUrl())
                    .build();

            Member member = Member.builder()
                .username(username)
                .name(oAuth2Response.getName())
                .profileImage(profileImage)
                .role("ROLE_USER")
                .build();

            Member savedMember = memberRepository.save(member);

            memberSecurityDto = new MemberSecurityDto(savedMember.getId(), savedMember.getRole(),
                savedMember.getName(), savedMember.getUsername(), false);
        } else {
            Member foundMember = foundMemberOptional.get();
            memberSecurityDto = new MemberSecurityDto(foundMember.getId(), foundMember.getRole(),
                foundMember.getName(), foundMember.getUsername(), foundMember.getGroup() != null);
        }

        return new CustomOAuth2User(memberSecurityDto);
    }
}
