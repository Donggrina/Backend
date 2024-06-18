package com.codeit.donggrina.domain.member.service;

import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import com.codeit.donggrina.domain.ProfileImage.repository.ProfileImageRepository;
import com.codeit.donggrina.domain.member.dto.request.MemberUpdateRequest;
import com.codeit.donggrina.domain.member.dto.response.MyProfileGetResponse;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Value("${image.url.default.member}")
    private String MEMBER_DEFAULT_IMAGE;
    private final MemberRepository memberRepository;
    private final ProfileImageRepository profileImageRepository;

    public MyProfileGetResponse getMyProfile(Long memberId) {
        return memberRepository.findMyProfileById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));
    }

    @Transactional
    public void updateMyProfile(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        Member currentMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        ProfileImage updateImage = null;
        if(memberUpdateRequest.imageId() == null) {
            updateImage = currentMember.getProfileImage();
            updateImage.updateUrl(MEMBER_DEFAULT_IMAGE);
        }else {
            updateImage = profileImageRepository.findById(
                    memberUpdateRequest.imageId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이미지입니다."));
        }

        currentMember.updateNickname(memberUpdateRequest.nickname());
        currentMember.updateProfileImage(updateImage);
    }
}
