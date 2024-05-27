package com.codeit.donggrina.domain.group.service;

import com.codeit.donggrina.common.util.SecurityUtil;
import com.codeit.donggrina.domain.group.dto.request.GroupAppendRequest;
import com.codeit.donggrina.domain.group.dto.request.GroupMemberAddRequest;
import com.codeit.donggrina.domain.group.entity.Group;
import com.codeit.donggrina.domain.group.repository.GroupRepository;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long append(GroupAppendRequest request) {
        String name = request.name();
        groupRepository.findByName(name).ifPresent(group -> {
            throw new IllegalArgumentException("이미 존재하는 그룹 이름입니다.");
        });
        String creatorName = request.creatorName();
        String code = generateRandomInvitationCode();
        Group group = Group.builder()
            .name(name)
            .code(code)
            .creatorName(creatorName)
            .build();
        return groupRepository.save(group).getId();
    }

    @Transactional
    public void addMember(Long groupId, GroupMemberAddRequest request) {
        // 그룹 식별자로 그룹을 조회한 후에 사용자가 입력한 코드가 그룹의 코드와 일치하는지 확인합니다.
        String code = request.code();
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));
        if (!group.getCode().equals(code)) {
            throw new IllegalArgumentException("초대 코드가 일치하지 않습니다.");
        }
        // 현재 가족에 들어가기 위해 로그인 한 사용자(초대받은 사람)를 그룹에 추가해줍니다.
        Member member = memberRepository.findById(SecurityUtil.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        group.addMember(member);
    }

    private String generateRandomInvitationCode() {
        int codeLength = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();

        while (true) {
            String code = IntStream.range(0, codeLength)
                .map(i -> random.nextInt(characters.length()))
                .mapToObj(characters::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

            // 코드가 이미 존재하는지 확인하고 존재하지 않으면 코드를 반환, 존재하면 while문을 다시 타면서 코드 다시 생성
            if (groupRepository.findByCode(code).isEmpty()) {
                return code;
            }
        }
    }
}
