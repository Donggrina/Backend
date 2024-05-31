package com.codeit.donggrina.domain.pet.entity;

import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import com.codeit.donggrina.domain.group.entity.Group;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
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
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Convert(converter = SexValueConverter.class)
    private Sex sex;
    private LocalDate birthDate;
    private LocalDate adoptionDate;
    private String type;
    private String species;
    private double weight;
    private boolean isNeutered;
    private String registrationNumber;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    @OneToOne
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    @Builder
    private Pet(Long id, String name, Sex sex, LocalDate birthDate, LocalDate adoptionDate,
        String type,
        String species, double weight, boolean isNeutered, String registrationNumber, Group group,
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
        this.registrationNumber = registrationNumber;
        this.group = group;
        this.profileImage = profileImage;
    }
}
