package com.codeit.donggrina.domain.growth_history.service;

import com.codeit.donggrina.common.api.SearchFilter;
import com.codeit.donggrina.domain.group.entity.Group;
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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrowthHistoryService {

    private final GrowthHistoryRepository growthHistoryRepository;
    private final MemberRepository memberRepository;

    public List<GrowthHistoryListResponse> getByDate(Long memberId, LocalDate date) {
        // 로그인 한 사용자와 소속 그룹을 데이터베이스에서 조회합니다. 멤버가 없거나 멤버가 그룹에 속해 있지 않다면 예외를 발생시킵니다.
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Group group = Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));
        Long groupId = group.getId();
        return growthHistoryRepository.findGrowthHistoryDetailByDate(groupId, date)
            .stream()
            .map(growthHistory -> {
                boolean isMine = growthHistory.getMember().equals(member);
                return GrowthHistoryListResponse.from(growthHistory, isMine);
            })
            .toList();
    }

    public GrowthHistoryDetailResponse getDetail(Long growthId, Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        GrowthHistory growthHistory = growthHistoryRepository.findGrowthHistoryDetail(growthId);
        boolean isMine = growthHistory.getMember().equals(member);
        return GrowthHistoryDetailResponse.from(growthHistory, isMine);
    }

    public List<GrowthHistoryListResponse> search(SearchFilter searchFilter, Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return growthHistoryRepository.findGrowthHistoryBySearchFilter(searchFilter)
            .stream()
            .map(growthHistory -> {
                boolean isMine = growthHistory.getMember().equals(member);
                return GrowthHistoryListResponse.from(growthHistory, isMine);
            })
            .toList();
    }

    @Transactional
    public Long append(Long memberId, GrowthHistoryAppendRequest request) {
        // 로그인 한 사용자와 소속 그룹을 데이터베이스에서 조회합니다. 멤버가 없거나 멤버가 그룹에 속해 있지 않다면 예외를 발생시킵니다.
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Group group = Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));

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
        // 로그인 한 사용자와 소속 그룹을 데이터베이스에서 조회합니다. 멤버가 없거나 멤버가 그룹에 속해 있지 않다면 예외를 발생시킵니다.
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Group group = Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));
        String groupCreator = group.getCreator();

        // GrowthHistory를 조회하고 수정합니다. 본인이 작성한 성장기록이 아니고 방장도 아니라면 예외가 발생합니다.
        GrowthHistory growthHistory = growthHistoryRepository.findById(growthId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 성장기록입니다."));
        if (!growthHistory.getMember().getId().equals(memberId) && !member.getUsername()
            .equals(groupCreator)) {
            throw new IllegalArgumentException("본인의 성장기록만 수정할 수 있습니다.");
        }
        growthHistory.update(request);
    }

    @Transactional
    public void delete(Long memberId, Long growthId) {
        // 로그인 한 사용자와 소속 그룹을 데이터베이스에서 조회합니다. 멤버가 없거나 멤버가 그룹에 속해 있지 않다면 예외를 발생시킵니다.
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Group group = Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));
        String groupCreator = group.getCreator();

        // GrowthHistory를 조회하고 삭제합니다. 본인이 작성한 성장기록이 아니고 방장도 아니라면 예외가 발생합니다.
        GrowthHistory growthHistory = growthHistoryRepository.findById(growthId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 성장기록입니다."));
        if (!growthHistory.getMember().getId().equals(memberId) && !member.getUsername()
            .equals(groupCreator)) {
            throw new IllegalArgumentException("본인의 성장기록만 삭제할 수 있습니다.");
        }
        growthHistoryRepository.delete(growthHistory);
    }
}
