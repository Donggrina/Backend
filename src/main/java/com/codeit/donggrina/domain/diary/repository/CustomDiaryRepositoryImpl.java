package com.codeit.donggrina.domain.diary.repository;

import static com.codeit.donggrina.domain.diary.entity.QDiary.diary;
import static com.codeit.donggrina.domain.diary.entity.QDiaryPet.diaryPet;
import static com.codeit.donggrina.domain.member.entity.QMember.member;
import static com.codeit.donggrina.domain.pet.entity.QPet.pet;

import com.codeit.donggrina.domain.diary.dto.request.DiarySearchRequest;
import com.codeit.donggrina.domain.diary.entity.Diary;
import com.codeit.donggrina.domain.group.entity.Group;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
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

    @Override
    public List<Diary> searchDiaries(DiarySearchRequest request, Group group) {
        return queryFactory.selectFrom(diary)
            .leftJoin(diary.member, member).fetchJoin()
            .leftJoin(diary.diaryImages).fetchJoin()
            .leftJoin(diary.diaryPets, diaryPet).fetchJoin()
            .leftJoin(diaryPet.pet, pet).fetchJoin()
            .leftJoin(pet.profileImage).fetchJoin()
            .where(
                eqGroup(group),
                eqAuthor(request.authors()),
                eqPet(request.petIds()),
                eqKeyword(request.keyword())
            )
            .fetch();
    }

    private BooleanExpression eqGroup(Group group) {
        return diary.group.eq(group);
    }

    private BooleanBuilder eqAuthor(List<String> authors) {
        if (authors == null) {
            return null;
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String author : authors) {
            booleanBuilder.or(member.nickname.containsIgnoreCase(author));
        }

        return booleanBuilder;
    }

    private BooleanBuilder eqPet(List<Long> petIds) {
        if (petIds == null) {
            return null;
        }
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (Long petId : petIds) {
            booleanBuilder.or(pet.id.eq(petId));
        }

        return booleanBuilder;
    }

    private BooleanExpression eqKeyword(String keyword) {
        return keyword == null ? null : diary.content.contains(keyword);
    }

}
