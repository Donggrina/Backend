package com.codeit.donggrina.domain.diary.repository;

import com.codeit.donggrina.domain.diary.entity.Diary;

public interface CustomDiaryRepository {

    void addLikeCount(Diary diary);

    void subLikeCount(Diary diary);

}
