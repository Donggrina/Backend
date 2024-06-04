package com.codeit.donggrina.domain.growth_history.service;

import com.codeit.donggrina.domain.group.entity.Group;
import com.codeit.donggrina.domain.group.repository.GroupRepository;
import com.codeit.donggrina.domain.growth_history.dto.request.GrowthHistoryAppendRequest;
import com.codeit.donggrina.domain.growth_history.dto.request.GrowthHistoryUpdateRequest;
import com.codeit.donggrina.domain.growth_history.dto.response.GrowthHistoryDetailResponse;
import com.codeit.donggrina.domain.growth_history.dto.response.GrowthHistoryListResponse;
import com.codeit.donggrina.domain.growth_history.entity.GrowthHistory;
import com.codeit.donggrina.domain.growth_history.repository.GrowthHistoryRepository;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import com.codeit.donggrina.domain.pet.entity.Pet;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrowthHistoryService {

    private final GrowthHistoryRepository growthHistoryRepository;
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    public List<GrowthHistoryListResponse> getByDate(LocalDate date) {
        return growthHistoryRepository.findGrowthHistoryDetailByDate(date);
    }

    public GrowthHistoryDetailResponse getDetail(Long growthId) {
        return growthHistoryRepository.findGrowthHistoryDetail(growthId);
    }

    @Transactional
    public Long append(Long memberId, GrowthHistoryAppendRequest request) {
        // 로그인 한 사용자를 조회하고, 해당 사용자가 속한 그룹과 그룹에 속한 반려동물을 fetch Join 으로 함께 조회합니다.
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        if (member.getGroup() == null) {
            throw new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다.");
        }
        Long groupId = member.getGroup().getId();
        Group group = groupRepository.findWithPets(groupId);
        if (group == null) {
            throw new IllegalArgumentException("존재하지 않는 그룹입니다."); // fidnWithPets 결과가 Group 엔티티이므로 null 체크가 필요합니다.
        }

        // 그룹에 속한 반려동물들 중에서 사용자가 선택한 반려동물을 조회합니다.
        Pet pet = group.getPets().stream()
            .filter(p -> p.getName().equals(request.petName()))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("그룹에 추가되지 않은 반려동물입니다."));

        // GrowthHistory를 생성하고 저장합니다.
        GrowthHistory growthHistory = GrowthHistory.builder()
            .member(member)
            .pet(pet)
            .date(request.date())
            .category(request.category())
            .food(request.content().food())
            .snack(request.content().snack())
            .abnormalSymptom(request.content().abnormalSymptom())
            .hospitalName(request.content().hospitalName())
            .symptom(request.content().symptom())
            .diagnosis(request.content().diagnosis())
            .medicationMethod(request.content().medicationMethod())
            .price(request.content().price())
            .memo(request.content().memo())
            .build();
        return growthHistoryRepository.save(growthHistory).getId();
    }

    @Transactional
    public void update(Long memberId, Long growthId, GrowthHistoryUpdateRequest request) {
        // 로그인 한 사용자를 조회합니다.
        memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // GrowthHistory를 조회하고 수정합니다.
        GrowthHistory growthHistory = growthHistoryRepository.findById(growthId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 성장기록입니다."));
        if (!growthHistory.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("본인의 성장기록만 수정할 수 있습니다.");
        }
        growthHistory.update(request);
    }

    @Transactional
    public void delete(Long memberId, Long growthId) {
        // 로그인 한 사용자를 조회합니다.
        memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // GrowthHistory를 조회하고 삭제합니다.
        GrowthHistory growthHistory = growthHistoryRepository.findById(growthId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 성장기록입니다."));
        if (!growthHistory.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("본인의 성장기록만 삭제할 수 있습니다.");
        }
        growthHistoryRepository.delete(growthHistory);
    }
}
