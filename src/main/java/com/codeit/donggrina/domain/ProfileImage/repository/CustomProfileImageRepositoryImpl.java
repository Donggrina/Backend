package com.codeit.donggrina.domain.ProfileImage.repository;

import static com.codeit.donggrina.domain.ProfileImage.entity.QProfileImage.profileImage;
import static com.codeit.donggrina.domain.member.entity.QMember.member;
import static com.codeit.donggrina.domain.pet.entity.QPet.pet;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomProfileImageRepositoryImpl implements CustomProfileImageRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findUnlinkedProfileImageIds() {
        return queryFactory
            .select(profileImage.id)
            .from(profileImage)
            .leftJoin(member).on(profileImage.id.eq(member.profileImage.id))
            .leftJoin(pet).on(profileImage.id.eq(pet.profileImage.id))
            .where(member.profileImage.id.isNull().and(pet.profileImage.id.isNull()))
            .fetch();
    }
}
