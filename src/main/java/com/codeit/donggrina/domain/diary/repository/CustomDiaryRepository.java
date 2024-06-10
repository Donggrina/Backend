package com.codeit.donggrina.domain.diary.repository;

import com.codeit.donggrina.domain.diary.entity.Diary;

public interface CustomDiaryRepository {

    void addHeartCount(Diary diary);

    void subHeartCount(Diary diary);

}
