package com.codeit.donggrina.domain.pet.dto.request;

import com.codeit.donggrina.domain.pet.entity.Sex;
import com.codeit.donggrina.domain.pet.entity.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PetUpdateRequest(
    @NotNull(message = "이미지 아이디를 입력해주세요.")
    Long imageId,
    @NotBlank(message = "이름을 입력해주세요.")
    String name,
    Sex sex,
    @NotNull(message = "생일 입력해주세요.")
    LocalDate birthDate,
    @NotNull(message = "입양일을 입력해주세요.")
    LocalDate adoptionDate,
    Type type,
    @NotBlank(message = "품종을 입력해주세요.")
    String species,
    @NotNull(message = "몸무게를 입력해주세요.")
    Double weight,
    @NotNull(message = "중성화 여부를 입력해주세요.")
    Boolean isNeutered
) {

}
