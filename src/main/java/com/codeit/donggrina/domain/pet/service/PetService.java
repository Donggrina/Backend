package com.codeit.donggrina.domain.pet.service;

import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import com.codeit.donggrina.domain.ProfileImage.repository.ProfileImageRepository;
import com.codeit.donggrina.domain.group.entity.Group;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import com.codeit.donggrina.domain.pet.dto.request.PetAddRequest;
import com.codeit.donggrina.domain.pet.dto.request.PetUpdateRequest;
import com.codeit.donggrina.domain.pet.dto.response.PetFindListResponse;
import com.codeit.donggrina.domain.pet.dto.response.PetFindResponse;
import com.codeit.donggrina.domain.pet.entity.Pet;
import com.codeit.donggrina.domain.pet.entity.Type;
import com.codeit.donggrina.domain.pet.repository.PetRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final ProfileImageRepository profileImageRepository;
    @Value("${image.url.default.dog}")
    private String DOG_DEFAULT_IMAGE_URL;
    @Value("${image.url.default.cat}")
    private String CAT_DEFAULT_IMAGE_URL;

    @Transactional
    public void addPet(Long memberId, PetAddRequest petAddRequest) {
        Member currentMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        Optional.ofNullable(currentMember.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));

        ProfileImage image = null;
        if (petAddRequest.imageId() != null) {
            image = profileImageRepository.findById(petAddRequest.imageId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이미지입니다."));

        } else {
            String url = DOG_DEFAULT_IMAGE_URL;
            if (petAddRequest.type().equals(Type.CAT)) {
                url = CAT_DEFAULT_IMAGE_URL;
            }

            image = ProfileImage.builder()
                .name("pet_default_image")
                .url(url)
                .build();
        }

        Group myGroup = currentMember.getGroup();
        Pet pet = Pet.builder()
            .name(petAddRequest.name())
            .sex(petAddRequest.sex())
            .birthDate(petAddRequest.birthDate())
            .adoptionDate(petAddRequest.adoptionDate())
            .type(petAddRequest.type())
            .species(petAddRequest.species())
            .weight(petAddRequest.weight())
            .isNeutered(petAddRequest.isNeutered())
            .group(myGroup)
            .profileImage(image)
            .build();

        myGroup.addPet(pet);
    }

    public List<PetFindListResponse> findPetList(Long memberId) {
        Member currentMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        Optional.ofNullable(currentMember.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));

        List<Pet> pets = currentMember.getGroup().getPets();
        return pets.stream()
            .map(PetFindListResponse::from)
            .collect(Collectors.toList());
    }

    public PetFindResponse findPet(Long petId) {
        return petRepository.findPetResponseById(petId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 반려동물입니다."));
    }

    @Transactional
    public void deletePet(Long petId) {
        Pet targetPet = petRepository.findById(petId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 반려동물입니다."));
        petRepository.delete(targetPet);
    }

    @Transactional
    public void updatePet(Long petId, PetUpdateRequest petUpdateRequest) {
        Pet targetPet = petRepository.findById(petId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 반려동물입니다."));

        ProfileImage updateProfileImage = null;
        if(petUpdateRequest.imageId() == null) {
            String url = DOG_DEFAULT_IMAGE_URL;
            updateProfileImage = targetPet.getProfileImage();
            if(petUpdateRequest.type().equals(Type.CAT)) {
                url = CAT_DEFAULT_IMAGE_URL;
            }

            updateProfileImage.updateUrl(url);
        } else {
            updateProfileImage = profileImageRepository.findById(
                    petUpdateRequest.imageId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이미지입니다."));
        }

        targetPet.update(petUpdateRequest.name(), petUpdateRequest.sex(),
            petUpdateRequest.birthDate(),
            petUpdateRequest.adoptionDate(), petUpdateRequest.type(), petUpdateRequest.species(),
            petUpdateRequest.weight(), petUpdateRequest.isNeutered(), updateProfileImage);
    }
}
