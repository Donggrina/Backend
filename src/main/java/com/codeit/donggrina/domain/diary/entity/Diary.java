package com.codeit.donggrina.domain.diary.entity;

import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.pet.entity.Pet;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    List<DiaryPet> diaryPets = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "diary", orphanRemoval = true)
    List<DiaryImage> diaryImages = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String weather;
    private boolean isShared;
    private LocalDate date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Diary(Long id, String content, String weather, boolean isShared, LocalDate date,
        Member member, List<Pet> pets, List<DiaryImage> diaryImages) {
        this.id = id;
        this.content = content;
        this.weather = weather;
        this.isShared = isShared;
        this.date = date;
        this.member = member;
        this.diaryPets = convertToDiaryPets(pets);
        setDiary(diaryImages);
    }

    private List<DiaryPet> convertToDiaryPets(List<Pet> pets) {
        return pets.stream()
            .map((p) -> DiaryPet.builder()
                .pet(p)
                .diary(this)
                .build()
            )
            .collect(Collectors.toList());
    }

    private void setDiary(List<DiaryImage> diaryImages) {
        diaryImages.forEach(diaryImage -> diaryImage.setDiary(this));
    }
}
