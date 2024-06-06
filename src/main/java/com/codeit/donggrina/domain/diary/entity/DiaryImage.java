package com.codeit.donggrina.domain.diary.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    @ManyToOne
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Builder
    private DiaryImage(Long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
    }
}
