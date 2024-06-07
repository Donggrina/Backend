package com.codeit.donggrina.domain.diary.service;

import com.codeit.donggrina.domain.diary.dto.request.DiaryCreateRequest;
import com.codeit.donggrina.domain.diary.dto.request.DiaryUpdateRequest;
import com.codeit.donggrina.domain.diary.entity.Diary;
import com.codeit.donggrina.domain.diary.entity.DiaryImage;
import com.codeit.donggrina.domain.diary.repository.DiaryImageRepository;
import com.codeit.donggrina.domain.diary.repository.DiaryRepository;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import com.codeit.donggrina.domain.pet.entity.Pet;
import com.codeit.donggrina.domain.pet.repository.PetRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final PetRepository petRepository;
    private final DiaryImageRepository diaryImageRepository;

    @Transactional
    public void createDiary(DiaryCreateRequest diaryCreateRequest, Long memberId) {
        Member currentMember = memberRepository.findById(memberId)
            .orElseThrow(RuntimeException::new);

        List<DiaryImage> images = diaryCreateRequest.images().stream()
            .map((imageId) ->
                diaryImageRepository.findById(imageId).orElseThrow(RuntimeException::new))
            .toList();

        List<Pet> pets = diaryCreateRequest.pets().stream()
            .map((id) ->
                petRepository.findById(id).orElseThrow(RuntimeException::new)
            )
            .toList();

        Diary diary = Diary.builder()
            .content(diaryCreateRequest.content())
            .weather(diaryCreateRequest.weather())
            .isShared(diaryCreateRequest.isShare())
            .diaryImages(images)
            .member(currentMember)
            .pets(pets)
            .date(diaryCreateRequest.date())
            .build();

        diaryRepository.save(diary);

        if (diaryCreateRequest.isShare()) {
            // ToDo: 스토리 추가 로직
        }
    }

    @Transactional
    public void updateDiary(Long diaryId, DiaryUpdateRequest diaryUpdateRequest, Long memberId) {
        Diary targetDiary = diaryRepository.findById(diaryId).orElseThrow(RuntimeException::new);

        if (targetDiary.getMember().getId() != memberId) {
            throw new RuntimeException("수정 불가");
        }

        List<Pet> pets = diaryUpdateRequest.pets().stream()
            .map((id) ->
                petRepository.findById(id).orElseThrow(RuntimeException::new)
            )
            .toList();

        List<DiaryImage> images = diaryUpdateRequest.images().stream()
            .map((imageId) ->
                diaryImageRepository.findById(imageId).orElseThrow(RuntimeException::new))
            .toList();

        targetDiary.update(diaryUpdateRequest.content(), diaryUpdateRequest.weather(),
            diaryUpdateRequest.isShare(), diaryUpdateRequest.date(), pets, images);
    }
}
