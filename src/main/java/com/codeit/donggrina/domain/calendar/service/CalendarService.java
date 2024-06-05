package com.codeit.donggrina.domain.calendar.service;

import com.codeit.donggrina.domain.calendar.dto.request.CalendarAppendRequest;
import com.codeit.donggrina.domain.calendar.entity.Calendar;
import com.codeit.donggrina.domain.calendar.repository.CalendarRepository;
import com.codeit.donggrina.domain.group.entity.Group;
import com.codeit.donggrina.domain.group.repository.GroupRepository;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import com.codeit.donggrina.domain.pet.entity.Pet;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    @Transactional
    public Long append(Long memberId, CalendarAppendRequest request) {
        // 로그인 한 사용자를 데이터베이스에서 조회합니다.
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 사용자가 속한 그룹을 조회하고, 그룹에 속한 반려동물 중 일정을 등록하려는 반려동물을 찾습니다.
        if (member.getGroup() == null) {
            throw new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다.");
        }
        Long groupId = member.getGroup().getId();
        Group group = groupRepository.findWithPets(groupId);
        if (group == null) {
            throw new IllegalArgumentException(
                "존재하지 않는 그룹입니다."); // fidnWithPets 결과가 Group 엔티티이므로 null 체크가 필요합니다.
        }

        // 그룹에 속한 반려동물들 중에서 사용자가 선택한 반려동물을 조회합니다.
        Pet pet = group.getPets().stream()
            .filter(p -> p.getName().equals(request.petName()))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("그룹에 추가되지 않은 반려동물입니다."));

        // 일정을 추가하고 일정의 ID를 반환합니다.
        Calendar calendar = Calendar.builder()
            .title(request.title())
            .memo(request.memo())
            .category(request.category())
            .dateTime(LocalDateTime.parse(request.dateTime()))
            .pet(pet)
            .member(member)
            .build();
        return calendarRepository.save(calendar).getId();
    }
}
