package com.codeit.donggrina.domain.group.repository;

import com.codeit.donggrina.domain.group.entity.Group;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long>, CustomGroupRepository {

    Optional<Group> findByName(String name);

    Optional<Group> findByCode(String code);
}
