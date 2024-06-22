package com.codeit.donggrina.domain.comment.entity;

import com.codeit.donggrina.common.Timestamp;
import com.codeit.donggrina.domain.diary.entity.Diary;
import com.codeit.donggrina.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_member_comment"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_parent_comment"))
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private final List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false, foreignKey = @ForeignKey(name = "fk_diary_comment"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Diary diary;

    @Builder
    private Comment(String content, Member member, Diary diary) {
        this.date = LocalDate.now();
        this.content = content;
        this.member = member;
        this.diary = diary;
    }

    public void updateParent(Comment parentComment) {
        this.parent = parentComment;
    }

    public void addChildComment(Comment childComment) {
        this.children.add(childComment);
        childComment.updateParent(this);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
