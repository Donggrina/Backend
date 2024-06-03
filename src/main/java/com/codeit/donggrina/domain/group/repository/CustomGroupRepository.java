package com.codeit.donggrina.domain.group.repository;

import com.codeit.donggrina.domain.group.dto.response.GroupDetailResponse;
import com.codeit.donggrina.domain.group.entity.Group;

public interface CustomGroupRepository {

    GroupDetailResponse findGroupDetail(Long groupId);

    Group findWithPets(Long groupId);
}
