package com.codeit.donggrina.domain.diary.repository;

import static com.codeit.donggrina.domain.diary.entity.QDiary.diary;

import com.codeit.donggrina.domain.diary.entity.Diary;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomDiaryRepositoryImpl implements CustomDiaryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public void addHeartCount(Diary selectedDiary) {
        queryFactory.update(diary)
            .set(diary.heartCount, diary.heartCount.add(1))
            .where(diary.eq(selectedDiary))
            .execute();
    }

    @Override
    public void subHeartCount(Diary selectedDiary) {
        queryFactory.update(diary)
            .set(diary.heartCount, diary.heartCount.subtract(1))
            .where(diary.eq(selectedDiary))
            .execute();
    }
}
