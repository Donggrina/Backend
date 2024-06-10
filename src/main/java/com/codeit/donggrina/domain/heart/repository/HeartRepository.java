package com.codeit.donggrina.domain.heart.repository;

import com.codeit.donggrina.domain.diary.entity.Diary;
import com.codeit.donggrina.domain.heart.entity.Heart;
import com.codeit.donggrina.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    Optional<Heart> findByMemberAndDiary(Member member, Diary diary);
}
