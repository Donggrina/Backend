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
    private String title;
    @Column(columnDefinition = "TEXT")
    private String memo;
    @Convert(converter = CalendarCategoryEnumConverter.class)
    private CalendarCategory category;
    private LocalDateTime dateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

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
}
