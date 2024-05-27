package com.codeit.donggrina.domain.group.service;

import com.codeit.donggrina.domain.group.dto.request.GroupAppendRequest;
import com.codeit.donggrina.domain.group.entity.Group;
import com.codeit.donggrina.domain.group.repository.GroupRepository;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final static int INVITATION_CODE_LENGTH = 8;
    private final static String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final GroupRepository groupRepository;

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

    private String generateRandomInvitationCode() {
        Random random = new Random();
        return IntStream.range(0, INVITATION_CODE_LENGTH)
            .map(i -> random.nextInt(CHARACTERS.length()))
            .mapToObj(CHARACTERS::charAt)
            .map(Object::toString)
            .collect(Collectors.joining());
    }
}
