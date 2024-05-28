package com.codeit.donggrina.domain.member.service;

import com.codeit.donggrina.domain.member.dto.response.MyProfileGetResponse;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MyProfileGetResponse getMyProfile(Long memberId) {
        return memberRepository.findMyProfileById(memberId).orElseThrow(RuntimeException::new);
    }
}
