package com.codeit.donggrina.domain.growth_history.entity;

import com.codeit.donggrina.common.Timestamp;
import com.codeit.donggrina.domain.growth_history.dto.request.GrowthHistoryUpdateRequest;
import com.codeit.donggrina.domain.growth_history.util.CategoryEnumConverter;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.pet.entity.Pet;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GrowthHistory extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_member_growth_history"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pet_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pet_growth_history"))
    private Pet pet;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    @Convert(converter = CategoryEnumConverter.class)
    private GrowthHistoryCategory category;

    private String food;

    private String snack;

    private String abnormalSymptom;

    private String hospitalName;

    private String symptom;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String medicationMethod;

    private Integer price;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Builder
    private GrowthHistory(Member member, Pet pet, LocalDate date, GrowthHistoryCategory category,
        String food, String snack, String abnormalSymptom, String hospitalName, String symptom,
        String diagnosis, String medicationMethod, Integer price, String memo) {
        this.member = member;
        this.pet = pet;
        this.date = date;
        this.category = category;
        this.food = food;
        this.snack = snack;
        this.abnormalSymptom = abnormalSymptom;
        this.hospitalName = hospitalName;
        this.symptom = symptom;
        this.diagnosis = diagnosis;
        this.medicationMethod = medicationMethod;
        this.price = price;
        this.memo = memo;
    }

    public void update(GrowthHistoryUpdateRequest request) {
        this.date = request.date();
        this.category = request.category();
        this.food = request.content().food();
        this.snack = request.content().snack();
        this.abnormalSymptom = request.content().abnormalSymptom();
        this.hospitalName = request.content().hospitalName();
        this.symptom = request.content().symptom();
        this.diagnosis = request.content().diagnosis();
        this.medicationMethod = request.content().medicationMethod();
        this.price = request.content().price();
        this.memo = request.content().memo();
    }
}
