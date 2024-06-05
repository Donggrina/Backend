package com.codeit.donggrina.domain.diary.repository;

import com.codeit.donggrina.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

}
