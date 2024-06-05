package com.codeit.donggrina.domain.ProfileImage.repository;

import java.util.List;

public interface CustomProfileImageRepository {

    List<Long> findUnlinkedProfileImageIds();
}
