package com.codeit.donggrina.domain.pet.repository;

import com.codeit.donggrina.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

}
