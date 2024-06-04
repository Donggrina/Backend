package com.codeit.donggrina.domain.pet.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import com.codeit.donggrina.domain.pet.dto.request.PetAddRequest;
import com.codeit.donggrina.domain.pet.dto.request.PetUpdateRequest;
import com.codeit.donggrina.domain.pet.dto.response.PetFindListResponse;
import com.codeit.donggrina.domain.pet.dto.response.PetFindResponse;
import com.codeit.donggrina.domain.pet.service.PetService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping("/my/pets")
    public ApiResponse<Void> addPet(@AuthenticationPrincipal CustomOAuth2User user,
        @RequestBody @Validated PetAddRequest petAddRequest) {

        petService.addPet(user.getMemberId(), petAddRequest);
        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("반려동물 등록 성공")
            .build();
    }

    @GetMapping("/my/pets")
    public ApiResponse<List<PetFindListResponse>> findPetList(
        @AuthenticationPrincipal CustomOAuth2User user) {

        return ApiResponse.<List<PetFindListResponse>>builder()
            .code(HttpStatus.OK.value())
            .message("반려동물 전체 조회 성공")
            .data(petService.findPetList(user.getMemberId()))
            .build();
    }

    @GetMapping("/my/pets/{petId}")
    public ApiResponse<PetFindResponse> findPetList(@PathVariable Long petId) {

        return ApiResponse.<PetFindResponse>builder()
            .code(HttpStatus.OK.value())
            .message("반려동물 조회 성공")
            .data(petService.findPet(petId))
            .build();
    }

    @DeleteMapping("/my/pets/{petId}")
    public ApiResponse<Void> deletePet(@PathVariable Long petId) {
        petService.deletePet(petId);

        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("반려동물 삭제 성공")
            .build();
    }

    @PutMapping("my/pets/{petId}")
    public ApiResponse<Void> updatePet(@PathVariable @Validated Long petId,
        @RequestBody PetUpdateRequest petUpdateRequest) {
        petService.updatePet(petId, petUpdateRequest);

        return ApiResponse.<Void>builder()
            .code(HttpStatus.OK.value())
            .message("반려동물 수정 성공")
            .build();
    }

}
