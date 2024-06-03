package com.codeit.donggrina.domain.pet.dto.response;

import com.codeit.donggrina.domain.pet.entity.Pet;
import com.codeit.donggrina.domain.pet.entity.Sex;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record PetFindResponse(
    String petProfileImageUrl,
    String name,
    Sex sex,
    LocalDate birthDate,
    LocalDate adoptionDate,
    String type,
    String species,
    double weight,
    boolean isNeutered,
    String registrationNumber
) {
    public static PetFindResponse from(Pet pet) {
        return PetFindResponse.builder()
            .petProfileImageUrl(pet.getProfileImage().getUrl())
            .name(pet.getName())
            .sex(pet.getSex())
            .birthDate(pet.getBirthDate())
            .adoptionDate(pet.getAdoptionDate())
            .type(pet.getType())
            .species(pet.getSpecies())
            .weight(pet.getWeight())
            .isNeutered(pet.isNeutered())
            .registrationNumber(pet.getRegistrationNumber())
            .build();
    }
}
