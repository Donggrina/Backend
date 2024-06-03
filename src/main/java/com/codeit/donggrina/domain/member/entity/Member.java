package com.codeit.donggrina.domain.member.entity;

import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import com.codeit.donggrina.domain.group.entity.Group;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String name;
    private String role;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;
    private String nickname; // 그룹 내에서 사용할 닉네임
    @ManyToOne
    @JoinColumn(name = "clusters_id")
    private Group group;

    @Builder
    private Member(Long id, String username, String name, String role, ProfileImage profileImage,
        String nickname) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.role = role;
        this.profileImage = profileImage;
        this.nickname = nickname;
    }

    public void joinGroup(Group group) {
        this.group = group;
    }

    public void leaveGroup() {
        this.group = null;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }
}
