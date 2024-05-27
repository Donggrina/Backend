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
