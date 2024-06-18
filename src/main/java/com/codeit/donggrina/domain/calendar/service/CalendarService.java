package com.codeit.donggrina.domain.calendar.service;

import com.codeit.donggrina.common.api.SearchFilter;
import com.codeit.donggrina.domain.calendar.dto.request.CalendarAppendRequest;
import com.codeit.donggrina.domain.calendar.dto.request.CalendarUpdateRequest;
import com.codeit.donggrina.domain.calendar.dto.response.CalendarDailyCountResponse;
import com.codeit.donggrina.domain.calendar.dto.response.CalendarDetailResponse;
import com.codeit.donggrina.domain.calendar.dto.response.CalendarListResponse;
import com.codeit.donggrina.domain.calendar.entity.Calendar;
import com.codeit.donggrina.domain.calendar.repository.CalendarRepository;
import com.codeit.donggrina.domain.group.entity.Group;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import com.codeit.donggrina.domain.pet.entity.Pet;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final MemberRepository memberRepository;

    public List<CalendarDailyCountResponse> getDailyCountByMonth(Long memberId,
        YearMonth yearMonth) {
        // 로그인 한 사용자와 소속 그룹을 데이터베이스에서 조회합니다. 멤버가 없거나 멤버가 그룹에 속해 있지 않다면 예외를 발생시킵니다.
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Group group = Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));
        Long groupId = group.getId();

        if (yearMonth == null) {
            yearMonth = YearMonth.now();
        }
        return calendarRepository.getDailyCountByMonth(groupId, yearMonth);
    }

    public List<CalendarListResponse> getDayListByDate(Long memberId, LocalDate date) {
        // 로그인 한 사용자와 소속 그룹을 데이터베이스에서 조회합니다. 멤버가 없거나 멤버가 그룹에 속해 있지 않다면 예외를 발생시킵니다.
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Group group = Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));
        Long groupId = group.getId();

        if (date == null) {
            date = LocalDate.now();
        }
        return calendarRepository.getDayListByDate(groupId, date)
            .stream()
            .map(calendar -> {
                boolean isMine = calendar.getMember().equals(member);
                return CalendarListResponse.from(calendar, isMine);
            })
            .toList();
    }

    public CalendarDetailResponse getDetail(Long calendarId, Long memberId) {
        // 로그인 한 유저를 조회합니다.
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));

        // 일정을 조회하고, 조회한 일정이 로그인한 유저가 작성한 일정인지 확인합니다.
        Calendar findCalendar = calendarRepository.getDetail(calendarId);
        boolean isMine = findCalendar.getMember().equals(member);

        return CalendarDetailResponse.from(findCalendar, isMine);
    }

    public List<CalendarListResponse> search(SearchFilter searchFilter, Long memberId) {
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));

        return calendarRepository.findBySearchFilter(searchFilter)
            .stream()
            .map(calendar -> {
                boolean isMine = calendar.getMember().equals(member);
                return CalendarListResponse.from(calendar, isMine);
            })
            .toList();
    }

    @Transactional
    public Long append(Long memberId, CalendarAppendRequest request) {
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

        // 일정을 추가하고 일정의 ID를 반환합니다.
        Calendar calendar = Calendar.builder()
            .title(request.title())
            .memo(request.memo())
            .category(request.category())
            .dateTime(request.getLocalDateTime())
            .pet(pet)
            .member(member)
            .build();
        return calendarRepository.save(calendar).getId();
    }

    @Transactional
    public void update(Long memberId, Long calendarId, CalendarUpdateRequest request) {
        // 로그인 한 사용자와 소속 그룹을 데이터베이스에서 조회합니다. 멤버가 없거나 멤버가 그룹에 속해 있지 않다면 예외를 발생시킵니다.
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Group group = Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));

        // 그룹의 방장의 이름을 조회합니다.
        String groupCreator = group.getCreator();

        // 그룹에 속한 반려동물들 중에서 사용자가 선택한 반려동물을 조회합니다.
        Pet pet = group.getPets().stream()
            .filter(p -> p.getName().equals(request.petName()))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("그룹에 추가되지 않은 반려동물입니다."));

        // 일정을 조회하고 수정합니다. 본인이 작성한 일정이 아니거나 그룹 방장이 아니라면 예외를 발생시킵니다.
        Calendar calendar = calendarRepository.findById(calendarId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));
        if (!calendar.getMember().getId().equals(memberId) && !member.getUsername()
            .equals(groupCreator)) {
            throw new IllegalArgumentException("본인이 작성한 일정만 수정할 수 있습니다.");
        }
        calendar.update(request, pet);
    }

    @Transactional
    public void updateCompletionState(Long memberId, Long calendarId) {
        // 로그인 한 사용자와 소속 그룹을 데이터베이스에서 조회합니다. 멤버가 없거나 멤버가 그룹에 속해 있지 않다면 예외를 발생시킵니다.
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Group group = Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));
        String groupCreator = group.getCreator();

        // 일정을 조회하고 완료 여부를 수정합니다. 본인이 작성한 일정이 아니거나 방장이 아니라면 예외를 발생시킵니다.
        Calendar calendar = calendarRepository.findById(calendarId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));
        if (!calendar.getMember().getId().equals(memberId) && !member.getUsername()
            .equals(groupCreator)) {
            throw new IllegalArgumentException("본인이 작성한 일정만 완료 처리 할 수 있습니다.");
        }
        calendar.updateCompletion();
    }

    @Transactional
    public void delete(Long memberId, Long calendarId) {
        // 로그인 한 사용자와 소속 그룹을 데이터베이스에서 조회합니다. 멤버가 없거나 멤버가 그룹에 속해 있지 않다면 예외를 발생시킵니다.
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Group group = Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));
        String groupCreator = group.getCreator();

        // 일정을 조회하고 삭제합니다. 본인이 작성한 일정이 아니거나 방장이 아니라면 예외를 발생시킵니다.
        Calendar calendar = calendarRepository.findById(calendarId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));
        if (!calendar.getMember().getId().equals(memberId) && !member.getUsername()
            .equals(groupCreator)) {
            throw new IllegalArgumentException("본인이 작성한 일정만 삭제할 수 있습니다.");
        }
        calendarRepository.delete(calendar);
    }
}
