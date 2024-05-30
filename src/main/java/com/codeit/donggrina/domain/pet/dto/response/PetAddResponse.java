package com.codeit.donggrina.domain.pet.dto.response;

import com.codeit.donggrina.domain.pet.entity.Pet;
import lombok.Builder;

@Builder
public record PetAddResponse(
    Long id,
    String name,
    String imageUrl
) {

    public static PetAddResponse from(Pet pet, String imageUrl) {
        return PetAddResponse.builder()
            .id(pet.getId())
            .name(pet.getName())
            .imageUrl(imageUrl)
            .build();
    }
}
