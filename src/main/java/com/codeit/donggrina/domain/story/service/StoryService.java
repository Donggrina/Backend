package com.codeit.donggrina.domain.story.service;

import com.codeit.donggrina.domain.diary.entity.Diary;
import com.codeit.donggrina.domain.diary.repository.DiaryRepository;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final DiaryRepository diaryRepository;

    @Transactional
    public void createStory(Long diaryId, Long memberId) {
        Diary targetDiary = diaryRepository.findByIdWithMember(diaryId)
            .orElseThrow(RuntimeException::new);

        if(!targetDiary.getMember().getId().equals(memberId)) {
            throw new RuntimeException();
        }

        targetDiary.shareToStory();
    }

    @Transactional
    public void deleteStory(Long diaryId, Long memberId) {
        Diary targetDiary = diaryRepository.findByIdWithMember(diaryId)
            .orElseThrow(RuntimeException::new);

        if(!targetDiary.getMember().getId().equals(memberId)) {
            throw new RuntimeException();
        }

        targetDiary.unShareToStory();
    }
}
