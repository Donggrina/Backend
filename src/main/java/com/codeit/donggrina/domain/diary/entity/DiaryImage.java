package com.codeit.donggrina.domain.diary.entity;

import com.codeit.donggrina.common.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryImage extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "diary_id", foreignKey = @ForeignKey(name = "fk_diary_diary_image"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Diary diary;

    @Builder
    private DiaryImage(Long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public void linkDiary(Diary diary) {
        this.diary = diary;
    }

    public void unLinkDiary() {
        this.diary = null;
    }
}
