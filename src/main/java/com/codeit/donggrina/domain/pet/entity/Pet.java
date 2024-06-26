package com.codeit.donggrina.domain.pet.entity;

import com.codeit.donggrina.common.Timestamp;
import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import com.codeit.donggrina.domain.group.entity.Group;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_profile_image_pet", columnNames = "profile_image_id")
    }
)
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
    @Convert(converter = TypeValueConverter.class)
    private Type type;

    @Column(nullable = false)
    private String species;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private boolean isNeutered;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clusters_id", nullable = false, foreignKey = @ForeignKey(name = "fk_clusters_pet"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Group group;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_image_id", nullable = false, foreignKey = @ForeignKey(name = "fk_profile_image_pet"))
    private ProfileImage profileImage;

    @Builder
    private Pet(Long id, String name, Sex sex, LocalDate birthDate, LocalDate adoptionDate,
        Type type, String species, double weight, boolean isNeutered, ProfileImage profileImage) {
        this.id = id;
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

    public void update(String name, Sex sex, LocalDate birthDate, LocalDate adoptionDate, Type type,
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

    public void joinGroup(Group group) {
        this.group = group;
    }
}
