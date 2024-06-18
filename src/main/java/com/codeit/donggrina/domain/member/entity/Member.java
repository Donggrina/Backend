package com.codeit.donggrina.domain.member.entity;

import com.codeit.donggrina.common.Timestamp;
import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import com.codeit.donggrina.domain.group.entity.Group;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_profile_image_member", columnNames = "profile_image_id")
    }
)
public class Member extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_image_id", nullable = false, foreignKey = @ForeignKey(name = "fk_profile_image_member"))
    private ProfileImage profileImage;

    private String nickname; // 그룹 내에서 사용할 닉네임
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clusters_id", foreignKey = @ForeignKey(name = "fk_clusters_member"))
    private Group group;

    private LocalDateTime joinGroupAt;

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
        this.joinGroupAt = LocalDateTime.now();
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
