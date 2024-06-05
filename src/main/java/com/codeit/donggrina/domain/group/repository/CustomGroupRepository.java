package com.codeit.donggrina.domain.group.repository;

import com.codeit.donggrina.domain.group.dto.response.GroupDetailResponse;
import com.codeit.donggrina.domain.group.entity.Group;
import java.util.Optional;

public interface CustomGroupRepository {

    GroupDetailResponse findGroupDetail(Long groupId);

    Optional<Group> findWithPets(Long groupId);
}
