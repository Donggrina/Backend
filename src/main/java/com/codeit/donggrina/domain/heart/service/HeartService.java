package com.codeit.donggrina.domain.heart.service;

import com.codeit.donggrina.domain.diary.entity.Diary;
import com.codeit.donggrina.domain.diary.repository.DiaryRepository;
import com.codeit.donggrina.domain.heart.entity.Heart;
import com.codeit.donggrina.domain.heart.repository.HeartRepository;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;

    @Transactional
    public void append(Long memberId, Long diaryId) {
        // 멤버와 좋아요를 누를 다이어리를 조회합니다.
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Diary diary = diaryRepository.findById(diaryId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다."));

        // 이미 좋아요를 누른 다이어리인지 확인하고 좋아요를 했다면 예외를 발생시킵니다.
        if (heartRepository.findByMemberAndDiary(member, diary).isPresent()) {
            throw new IllegalArgumentException("이미 좋아요를 누른 다이어리입니다.");
        }

        // 좋아요를 저장하고 다이어리의 좋아요 수를 +1 합니다.
        Heart heart = Heart.builder()
            .member(member)
            .diary(diary)
            .build();
        heartRepository.save(heart);
        diaryRepository.addHeartCount(diary);
    }

    @Transactional
    public void cancel(Long memberId, Long diaryId) {
        // 멤버와 좋아요를 누를 다이어리를 조회합니다.
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Diary diary = diaryRepository.findById(diaryId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다."));

        // 멤버가 해당 다이어리에 좋아요를 눌렀는지 조회하고 없다면 예외를 발생시킵니다.
        Heart heart = heartRepository.findByMemberAndDiary(member, diary)
            .orElseThrow(() -> new IllegalArgumentException("좋아요를 누른 이력이 없습니다."));

        // 좋아요를 삭제하고 다이어리의 좋아요 수를 -1 합니다.
        heartRepository.delete(heart);
        diaryRepository.subHeartCount(diary);
    }
}
