package com.codeit.donggrina.domain.diary.repository;

import com.codeit.donggrina.domain.diary.dto.request.DiarySearchRequest;
import com.codeit.donggrina.domain.diary.entity.Diary;
import java.util.List;

public interface CustomDiaryRepository {

    void addHeartCount(Diary diary);

    void subHeartCount(Diary diary);

    List<Diary> searchDiaries(DiarySearchRequest request);

}
