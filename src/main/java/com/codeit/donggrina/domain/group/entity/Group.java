package com.codeit.donggrina.domain.group.entity;

import com.codeit.donggrina.common.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "families")
public class Group extends Timestamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String code;
  @Column(nullable = false)
  private String creatorName;

  @Builder
  private Group(String name, String code, String creatorName) {
    this.name = name;
    this.code = code;
    this.creatorName = creatorName;
  }
}
