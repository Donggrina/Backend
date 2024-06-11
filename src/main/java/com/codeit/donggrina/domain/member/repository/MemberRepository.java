package com.codeit.donggrina.domain.member.repository;

import com.codeit.donggrina.domain.member.dto.response.MyProfileGetResponse;
import com.codeit.donggrina.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    @Query("select new com.codeit.donggrina.domain.member.dto.response"
        + ".MyProfileGetResponse(m.id, m.name, m.nickname, pi.url)"
        + " from Member m join ProfileImage pi on m.profileImage.id = pi.id where m.id=:memberId")
    Optional<MyProfileGetResponse> findMyProfileById(Long memberId);

    @Query("select m from Member m join fetch m.group join fetch m.profileImage where m.id = :memberId")
    Optional<Member> findByIdWithGroupAndProfileImage(Long memberId);

    @Query("select m from Member m join fetch m.group where m.id = :memberId")
    Optional<Member> findByIdWithGroup(Long memberId);

}
