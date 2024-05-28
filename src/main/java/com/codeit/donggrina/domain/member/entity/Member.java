package com.codeit.donggrina.domain.member.entity;

import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToOne;
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
    @OneToOne
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    @Builder
    private Member(Long id, String username, String name, String role, ProfileImage profileImage) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.role = role;
        this.profileImage = profileImage;
    }
}
