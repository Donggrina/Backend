package com.codeit.donggrina.domain.pet.dto.response;

import com.codeit.donggrina.domain.pet.entity.Pet;
import com.codeit.donggrina.domain.pet.entity.Sex;
import com.codeit.donggrina.domain.pet.entity.Type;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record PetFindResponse(
    Long petProfileImageId,
    String url,
    String name,
    Sex sex,
    LocalDate birthDate,
    LocalDate adoptionDate,
    Type type,
    String species,
    double weight,
    boolean isNeutered
) {
    public static PetFindResponse from(Pet pet) {
        return PetFindResponse.builder()
            .petProfileImageId(pet.getProfileImage().getId())
            .url(pet.getProfileImage().getUrl())
            .name(pet.getName())
            .sex(pet.getSex())
            .birthDate(pet.getBirthDate())
            .adoptionDate(pet.getAdoptionDate())
            .type(pet.getType())
            .species(pet.getSpecies())
            .weight(pet.getWeight())
            .isNeutered(pet.isNeutered())
            .build();
    }
}
