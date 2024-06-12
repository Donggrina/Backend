package com.codeit.donggrina.domain.pet.entity;

import com.codeit.donggrina.common.Timestamp;
import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import com.codeit.donggrina.domain.group.entity.Group;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Convert(converter = SexValueConverter.class)
    private Sex sex;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private LocalDate adoptionDate;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String species;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private boolean isNeutered;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clusters_id", nullable = false)
    private Group group;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    @Builder
    private Pet(Long id, String name, Sex sex, LocalDate birthDate, LocalDate adoptionDate,
        String type,
        String species, double weight, boolean isNeutered, Group group,
        ProfileImage profileImage) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birthDate = birthDate;
        this.adoptionDate = adoptionDate;
        this.type = type;
        this.species = species;
        this.weight = weight;
        this.isNeutered = isNeutered;
        this.group = group;
        this.profileImage = profileImage;
    }

    public void update(String name, Sex sex, LocalDate birthDate, LocalDate adoptionDate, String type,
        String species, double weight, boolean isNeutered, ProfileImage profileImage) {
        this.name = name;
        this.sex = sex;
        this.birthDate = birthDate;
        this.adoptionDate = adoptionDate;
        this.type = type;
        this.species = species;
        this.weight = weight;
        this.isNeutered = isNeutered;
        this.profileImage = profileImage;
    }
}
