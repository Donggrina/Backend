package com.codeit.donggrina.domain.group.repository;

import com.codeit.donggrina.domain.group.dto.response.GroupDetailResponse;

public interface CustomGroupRepository {

    GroupDetailResponse findGroupDetail(Long groupId);
}
