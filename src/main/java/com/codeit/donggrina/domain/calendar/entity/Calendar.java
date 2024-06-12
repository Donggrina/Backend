package com.codeit.donggrina.domain.calendar.entity;

import com.codeit.donggrina.common.Timestamp;
import com.codeit.donggrina.domain.calendar.dto.request.CalendarUpdateRequest;
import com.codeit.donggrina.domain.calendar.util.CalendarCategoryEnumConverter;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calendar extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String memo;

    @Column(nullable = false)
    @Convert(converter = CalendarCategoryEnumConverter.class)
    private CalendarCategory category;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pet_calendar"))
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_member_calendar"))
    private Member member;

    @Column(nullable = false)
    private boolean isFinished;

    @Builder
    private Calendar(String title, String memo, CalendarCategory category, LocalDateTime dateTime,
        Pet pet, Member member) {
        this.title = title;
        this.memo = memo;
        this.category = category;
        this.dateTime = dateTime;
        this.pet = pet;
        this.member = member;
    }

    public void update(CalendarUpdateRequest request, Pet pet) {
        this.title = request.title();
        this.memo = request.memo();
        this.category = request.category();
        this.dateTime = LocalDateTime.parse(request.dateTime());
        this.pet = pet;
    }

    public void updateCompletion() {
        this.isFinished = !this.isFinished;
    }
}
