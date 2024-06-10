package com.codeit.donggrina.domain.diary.repository;

import com.codeit.donggrina.domain.diary.entity.DiaryImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiaryImageRepository extends JpaRepository<DiaryImage, Long> {
    @Query("select di from DiaryImage di where di.diary = null")
    List<DiaryImage> findUnlinkedImages();

    @Query("delete from DiaryImage di where di.diary in :diaryImages")
    void deleteUnlinkedImages(List<DiaryImage> diaryImages);
}
