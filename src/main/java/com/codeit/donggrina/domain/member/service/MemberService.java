package com.codeit.donggrina.domain.member.service;

import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import com.codeit.donggrina.domain.ProfileImage.repository.ProfileImageRepository;
import com.codeit.donggrina.domain.member.dto.request.MemberUpdateRequest;
import com.codeit.donggrina.domain.member.dto.response.MyProfileGetResponse;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProfileImageRepository profileImageRepository;

    public MyProfileGetResponse getMyProfile(Long memberId) {
        return memberRepository.findMyProfileById(memberId).orElseThrow(RuntimeException::new);
    }

    @Transactional
    public void updateMyProfile(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        Member currentMember = memberRepository.findById(memberId)
            .orElseThrow(RuntimeException::new);
        ProfileImage updatedProfileImage = profileImageRepository.findById(
                memberUpdateRequest.imageId()).orElseThrow(RuntimeException::new);

        currentMember.updateNickname(memberUpdateRequest.name());
        currentMember.updateProfileImage(updatedProfileImage);
    }
}
