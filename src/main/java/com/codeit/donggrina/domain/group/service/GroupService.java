package com.codeit.donggrina.domain.group.service;

import com.codeit.donggrina.domain.group.dto.request.GroupAppendRequest;
import com.codeit.donggrina.domain.group.dto.request.GroupMemberAddRequest;
import com.codeit.donggrina.domain.group.dto.request.GroupUpdateRequest;
import com.codeit.donggrina.domain.group.dto.response.GroupCodeResponse;
import com.codeit.donggrina.domain.group.dto.response.GroupDetailResponse;
import com.codeit.donggrina.domain.group.entity.Group;
import com.codeit.donggrina.domain.group.repository.GroupRepository;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.RequestContextFilter;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final RequestContextFilter requestContextFilter;

    @Transactional(readOnly = true)
    public GroupDetailResponse getDetail(Long userId) {
        Member member = memberRepository.findByIdWithGroup(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));

        Long groupId = member.getGroup().getId();
        return groupRepository.findGroupDetail(groupId);
    }

    public GroupCodeResponse getGroupCode(Long memberId) {
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));

        Long groupId = member.getGroup().getId();
        String invitationCode = member.getGroup().getCode();
        return GroupCodeResponse.builder()
            .id(groupId)
            .code(invitationCode)
            .build();
    }

    @Transactional
    public Long append(GroupAppendRequest request, Long userId) {
        // 그룹 이름으로 조회를 하고 중복된 그룹이 있다면 예외를 발생시킵니다.
        String name = request.name();
        groupRepository.findByName(name).ifPresent(group -> {
            throw new IllegalArgumentException("이미 존재하는 그룹 이름입니다.");
        });

        // 로그인 한 사용자를 조회하고 해당 사용자가 입력한 닉네임으로 사용자 정보를 업데이트 합니다.
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        String nickname = request.nickname();
        member.updateNickname(nickname);

        // 초대 코드를 생성하고 그룹을 생성하고 생성한 사람을 그룹에 추가해주고 그룹을 DB에 저장합니다.
        String code = generateRandomInvitationCode();
        Group group = Group.builder()
            .name(name)
            .code(code)
            .creator(member.getUsername())
            .build();
        member.joinGroup(group);
        return groupRepository.save(group).getId();
    }

    @Transactional
    public void addMember(GroupMemberAddRequest request, Long userId) {
        // 단순하게 초대 코드로 조회해서 일치하는 그룹이 있으면 멤버를 추가합니다.
        // note 그런데 만약에 우연히 다른 오타로 원하지 않는 그룹의 초대코드를 입력하는 경우에 예상치 못 한 경우가 발생할 수도 있을 것 같습니다.(확률은 낮지만..?)
        String code = request.code();
        Group group = groupRepository.findByCode(code)
            .orElseThrow(() -> new IllegalArgumentException("올바른 초대 코드를 다시 입력해주세요."));

        // 현재 가족에 들어가기 위해 로그인 한 사용자(초대받은 사람)의 닉네임을 업데이트 하고 그룹에 추가해줍니다.
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        String nickname = request.nickname();
        member.updateNickname(nickname);
        group.addMember(member);
    }

    @Transactional
    public void update(GroupUpdateRequest request, Long userId) {
        Member member = memberRepository.findByIdWithGroup(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Group group = Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));

        // 그룹의 생성자와 로그인한 사용자가 일치하는지 확인하고 일치하지 않으면 예외를 발생시킵니다.
        if (!group.getCreator().equals(member.getUsername())) {
            throw new IllegalArgumentException("그룹을 수정할 권한이 없습니다.");
        }

        // 그룹 이름을 업데이트 합니다.
        group.updateName(request.name());
    }

    @Transactional
    public void delete(Long userId) {
        Member member = memberRepository.findByIdWithGroup(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Group group = Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));

        // 그룹의 생성자가 아닌 경우에는 그룹을 삭제할 수 없습니다.
        if (!group.getCreator().equals(member.getUsername())) {
            throw new IllegalArgumentException("그룹을 삭제할 권한이 없습니다.");
        }
        // 그룹에 방장 외의 멤버가 존재하면 그룹을 삭제할 수 없습니다.
        if (!group.isDeletable()) {
            throw new IllegalArgumentException("그룹에 멤버가 존재하면 그룹을 삭제할 수 없습니다.");
        }
        // 외래키 제약 조건 때문에 멤버와 그룹을 먼저 연관관계를 끊어주고 그룹을 삭제합니다.
        member.leaveGroup();
        groupRepository.delete(group);
    }

    @Transactional
    public void deleteMember(Long targetId, Long userId) {
        // 그룹과 로그인 유저, 삭제 대상 유저를 조회합니다.
        Member loginMember = memberRepository.findByIdWithGroup(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Group group = Optional.ofNullable(loginMember.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));
        Member targetMember = memberRepository.findById(targetId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 방장이 아니라면 멤버를 삭제할 수 없습니다.
        if (!loginMember.getUsername().equals(group.getCreator())) {
            throw new IllegalArgumentException("그룹 멤버를 삭제할 권한이 없습니다.");
        }
        // 그룹과 멤버의 연관관계를 끊어줌으로써 그룹에서 멤버를 삭제합니다.
        targetMember.leaveGroup();
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
