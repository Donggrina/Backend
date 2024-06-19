package com.codeit.donggrina.domain.diary.entity;

import com.codeit.donggrina.common.Timestamp;
import com.codeit.donggrina.domain.pet.entity.Pet;
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
public class DiaryPet extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "diary_id", nullable = false, foreignKey = @ForeignKey(name = "fk_diary_diary_pet"))
    private Diary diary;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pet_diary_pet"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Pet pet;

    @Builder
    private DiaryPet(Long id, Diary diary, Pet pet) {
        this.id = id;
        this.diary = diary;
        this.pet = pet;
    }

}
