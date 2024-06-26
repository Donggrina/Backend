package com.codeit.donggrina.domain.group.entity;

import com.codeit.donggrina.common.Timestamp;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.pet.entity.Pet;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "clusters")
public class Group extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String creator;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private final List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Pet> pets = new ArrayList<>();

    @Builder
    private Group(String name, String code, String creator) {
        this.name = name;
        this.code = code;
        this.creator = creator;
    }

    public void addMember(Member member) {
        members.add(member);
        member.joinGroup(this);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public boolean isDeletable() {
        return members.stream()
            .allMatch(member -> member.getUsername().equals(this.creator));
    }

    public void addPet(Pet pet) {
        this.pets.add(pet);
        pet.joinGroup(this);
    }
}
