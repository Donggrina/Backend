package com.codeit.donggrina.domain.diary.repository;

import com.codeit.donggrina.domain.diary.dto.response.DiaryFindResponse;
import com.codeit.donggrina.domain.diary.entity.Diary;
import com.codeit.donggrina.domain.group.entity.Group;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiaryRepository extends JpaRepository<Diary, Long>, CustomDiaryRepository {

    @Query("select d from Diary d"
        + " join fetch d.diaryPets dp"
        + " join fetch dp.pet p"
        + " join fetch p.profileImage"
        + " left join fetch d.diaryImages"
        + " where d.group = :group and d.date = :date")
    List<Diary> findAllByDate(LocalDate date, Group group);

    @Query("select d from Diary d"
        + " join fetch d.member m"
        + " join fetch m.profileImage mi"
        + " join fetch d.diaryPets dp"
        + " join fetch dp.pet p"
        + " join fetch p.profileImage pi"
        + " left join fetch d.diaryImages di"
        + " where d.id = :diaryId")
    Optional<Diary> findByIdWithDetails(Long diaryId);


    @Query("select d from Diary d left join fetch d.member where d.id = :diaryId")
    Optional<Diary> findByIdWithMember(Long diaryId);

    @Query("select d from Diary d"
        + " left join fetch d.diaryImages"
        + " left join fetch d.comments"
        + " left join fetch d.member m"
        + " left join fetch m.profileImage"
        + " left join fetch m.group"
        + " where d.id = :diaryId and d.isShared = true")
    Optional<Diary> findStoryWithDetails(Long diaryId);

    @Query("select d from Diary d"
        + " left join fetch d.diaryImages"
        + " left join fetch d.member m"
        + " left join fetch m.profileImage"
        + " left join fetch m.group"
        + " where d.isShared = true"
        + " order by d.id desc")
    Slice<Diary> findPage(Pageable pageable);
}

