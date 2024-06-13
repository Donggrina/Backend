package com.codeit.donggrina.domain.pet.dto.response;

import com.codeit.donggrina.domain.pet.entity.Pet;
import lombok.Builder;

@Builder
public record PetFindListResponse(
    Long petId,
    String imageUrl,
    String name
) {
    public static PetFindListResponse from(Pet pet) {
        return PetFindListResponse.builder()
            .petId(pet.getId())
            .imageUrl(pet.getProfileImage().getUrl())
            .name(pet.getName())
            .build();
    }
}
