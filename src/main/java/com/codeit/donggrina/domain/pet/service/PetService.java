package com.codeit.donggrina.domain.pet.service;

import com.codeit.donggrina.domain.group.entity.Group;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import com.codeit.donggrina.domain.pet.dto.request.PetAddRequest;
import com.codeit.donggrina.domain.pet.dto.response.PetFindListResponse;
import com.codeit.donggrina.domain.pet.dto.response.PetFindResponse;
import com.codeit.donggrina.domain.pet.entity.Pet;
import com.codeit.donggrina.domain.pet.repository.PetRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final MemberRepository memberRepository;
    private final PetRepository petRepository;

    @Transactional
    public void addPet(Long memberId, PetAddRequest petAddRequest) {
        Member currentMember = memberRepository.findById(memberId)
            .orElseThrow(RuntimeException::new);

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
            .registrationNumber(petAddRequest.registrationNumber())
            .group(myGroup)
            .build();

        myGroup.addPet(pet);
    }

    public List<PetFindListResponse> findPetList(Long memberId) {
        Member currentMember = memberRepository.findById(memberId)
            .orElseThrow(RuntimeException::new);

        List<Pet> pets = currentMember.getGroup().getPets();
        return pets.stream()
            .map(PetFindListResponse::from)
            .collect(Collectors.toList());
    }

    public PetFindResponse findPet(Long petId) {
        return petRepository.findPetResponseById(petId).orElseThrow(RuntimeException::new);
    }
}
