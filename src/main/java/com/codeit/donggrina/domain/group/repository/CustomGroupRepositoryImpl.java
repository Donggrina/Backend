package com.codeit.donggrina.domain.group.repository;

import static com.codeit.donggrina.domain.ProfileImage.entity.QProfileImage.profileImage;
import static com.codeit.donggrina.domain.member.entity.QMember.member;

import com.codeit.donggrina.domain.group.dto.response.GroupDetailResponse;
import com.codeit.donggrina.domain.group.entity.Group;
import com.codeit.donggrina.domain.group.entity.QGroup;
import com.codeit.donggrina.domain.member.dto.response.MyProfileGetResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomGroupRepositoryImpl implements CustomGroupRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public GroupDetailResponse findGroupDetail(Long groupId) {
        // Group 정보와 Member, ProfileImage 정보를 함께 조회
        Group group = queryFactory
            .selectFrom(QGroup.group)
            .leftJoin(QGroup.group.members, member).fetchJoin()
            .leftJoin(member.profileImage, profileImage).fetchJoin()
            .where(QGroup.group.id.eq(groupId))
            .distinct()
            .fetchOne();

        if (group == null) {
            throw new IllegalArgumentException("존재하지 않는 그룹입니다.");
        }

        // 멤버 수 카운트 쿼리
        Long membersCount = queryFactory
            .select(member.count())
            .from(member)
            .where(member.group.id.eq(groupId))
            .fetchOne();

        // MyProfileGetResponse 기반 members 생성
        List<MyProfileGetResponse> members = group.getMembers()
            .stream()
            .map(MyProfileGetResponse::from)
            .collect(Collectors.toList());

        return GroupDetailResponse
            .builder()
            .id(group.getId())
            .name(group.getName())
            .invitationCode(group.getCode())
            .members(members)
            .membersCount(membersCount)
            .build();
    }

    @Override
    public Optional<Group> findWithPets(Long groupId) {
        Group group = queryFactory
            .selectFrom(QGroup.group)
            .leftJoin(QGroup.group.pets).fetchJoin()
            .where(QGroup.group.id.eq(groupId))
            .fetchOne();
        return Optional.ofNullable(group);
    }
}
