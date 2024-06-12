package com.codeit.donggrina.domain.diary.entity;

import com.codeit.donggrina.common.Timestamp;
import com.codeit.donggrina.domain.comment.entity.Comment;
import com.codeit.donggrina.domain.group.entity.Group;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.pet.entity.Pet;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Diary extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String weather;

    @Column(nullable = false)
    private boolean isShared;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_member_diary"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clusters_id", nullable = false, foreignKey = @ForeignKey(name = "fk_group_diary"))
    private Group group;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(nullable = false)
    Set<DiaryPet> diaryPets = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "diary", orphanRemoval = true)
    List<DiaryImage> diaryImages = new ArrayList<>();

    @Column(nullable = false)
    private int heartCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "diary", cascade = CascadeType.REMOVE)
    private final List<Comment> comments = new ArrayList<>();

    @Builder
    private Diary(Long id, String content, String weather, boolean isShared, LocalDate date,
        Member member, Group group, List<Pet> pets, List<DiaryImage> diaryImages) {
        this.id = id;
        this.content = content;
        this.weather = weather;
        this.isShared = isShared;
        this.date = date;
        this.member = member;
        this.group = group;
        this.heartCount = 0;
        addDiaryPets(pets);
        linkDiaryImageToDiary(diaryImages);
    }

    private void addDiaryPets(List<Pet> pets) {
        List<DiaryPet> diaryPets = pets.stream()
            .map((pet) -> DiaryPet.builder()
                .pet(pet)
                .diary(this)
                .build()
            )
            .toList();

        this.diaryPets.addAll(diaryPets);
    }

    private void linkDiaryImageToDiary(List<DiaryImage> diaryImages) {
        diaryImages.forEach(diaryImage -> diaryImage.linkDiary(this));
    }

    private void unLinkDiaryImageToDiary(List<DiaryImage> diaryImages) {
        for(DiaryImage diaryImage : this.diaryImages) {
            if(!diaryImages.contains(diaryImage)) {
                diaryImage.unLinkDiary();
            }
        }
    }

    public void update(String content, String weather, boolean isShared, LocalDate date,
        List<Pet> pets, List<DiaryImage> images) {
        this.content = content;
        this.weather = weather;
        this.isShared = isShared;
        this.date = date;
        updatePets(pets);
        updateImages(images);
    }

    private void updatePets(List<Pet> pets) {
        diaryPets.clear();
        addDiaryPets(pets);
    }
    private void updateImages(List<DiaryImage> images){
        linkDiaryImageToDiary(images);
        unLinkDiaryImageToDiary(images);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

}
