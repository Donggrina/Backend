package com.codeit.donggrina.domain.pet.repository;

import com.codeit.donggrina.domain.pet.dto.response.PetFindResponse;
import com.codeit.donggrina.domain.pet.entity.Pet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("select new com.codeit.donggrina.domain.pet.dto.response.PetFindResponse(" +
        "pi.id, pi.url, p.name, p.sex, p.birthDate, p.adoptionDate, " +
        "p.type, p.species, p.weight, p.isNeutered) " +
        "from Pet p " +
        "join p.profileImage pi " +
        "WHERE p.id = :petId")
    Optional<PetFindResponse> findPetResponseById(Long petId);

}
