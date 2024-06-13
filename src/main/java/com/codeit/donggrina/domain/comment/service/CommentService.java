package com.codeit.donggrina.domain.comment.service;

import com.codeit.donggrina.domain.comment.dto.request.CommentAppendRequest;
import com.codeit.donggrina.domain.comment.dto.request.CommentUpdateRequest;
import com.codeit.donggrina.domain.comment.entity.Comment;
import com.codeit.donggrina.domain.comment.repository.CommentRepository;
import com.codeit.donggrina.domain.diary.entity.Diary;
import com.codeit.donggrina.domain.diary.repository.DiaryRepository;
import com.codeit.donggrina.domain.group.entity.Group;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long append(Long diaryId,CommentAppendRequest request, Long memberId) {
        // 댓글을 작성하는 로그인 멤버와 댓글이 달릴 다이어리를 조회합니다.
        Member member = memberRepository.findByIdWithGroup(memberId)
            .map(m -> {
                if (m.getGroup() == null) {
                    throw new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다.");
                }
                return m;
            })
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Diary diary = diaryRepository.findById(diaryId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다."));

        // 댓글을 생성합니다.
        Comment comment = Comment.builder()
            .member(member)
            .diary(diary)
            .content(request.content())
            .build();

        // 다이어리에 있는 댓글 리스트에 댓글을 추가해줍니다.
        diary.addComment(comment);

        // 부모 댓글이 있는 경우, 부모 댓글에 자식 댓글을 추가합니다.
        Optional.ofNullable(request.parentCommentId())
            .map(parentId -> commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부모 댓글입니다.")))
            .ifPresent(parentComment -> parentComment.addChildComment(comment));

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public void update(Long commentId, CommentUpdateRequest request, Long memberId) {
        // 댓글을 수정하는 로그인 멤버와 수정할 댓글을 조회합니다.
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 댓글을 수정합니다. 댓글을 작성한 사람이 아니라면 예외를 발생시킵니다.
        if (!comment.getMember().equals(member)) {
            throw new IllegalArgumentException("본인의 댓글만 수정할 수 있습니다.");
        }
        comment.updateContent(request.content());
    }

    @Transactional
    public void deleteParentComment(Long commentId, Long memberId) {
        // 댓글을 삭제하는 로그인 멤버와 삭제할 댓글을 조회합니다.
        Member member = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Group group = Optional.ofNullable(member.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));
        String groupCreator = group.getCreator();

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 댓글을 삭제합니다. 댓글을 작성한 사람이 아니고 방장도 아니라면 예외를 발생시킵니다.
        if (!comment.getMember().equals(member) && !member.getUsername().equals(groupCreator)) {
            throw new IllegalArgumentException("본인의 댓글만 삭제할 수 있습니다.");
        }

        // 대댓글이 하나도 달려있지 않은 댓글은 바로 삭제 합니다.
        if (comment.getChildren().isEmpty()) {
            commentRepository.delete(comment);
        } else {
            if (member.getUsername().equals(groupCreator)) {
                // 그룹의 생성자는 대댓글이 달린 댓글을 삭제할 수 있습니다. 이 경우에 대댓글도 함께 삭제됩니다.
                commentRepository.deleteAll(comment.getChildren());
                commentRepository.delete(comment);
            } else {
                // 그룹의 생성자가 아니라면 대댓글이 달린 댓글은 soft delete 됩니다. isDeleted 가 true 로 바뀌고 댓글 내용이 변경됩니다.
                comment.updateIsDeleted(true);
                comment.updateContent("삭제된 댓글입니다.");
            }
        }
    }

    @Transactional
    public void deleteChildComment(Long commentId, Long memberId) {
        // 댓글을 삭제하는 로그인 멤버와 삭제할 대댓글을 조회합니다.
        Member member = memberRepository.findByIdWithGroup(memberId)
            .map(m -> {
                if (m.getGroup() == null) {
                    throw new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다.");
                }
                return m;
            })
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // 대댓글을 삭제합니다. 대댓글을 작성한 사람이 아니라면 예외를 발생시킵니다.
        if (!comment.getMember().equals(member)) {
            throw new IllegalArgumentException("본인의 댓글만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }
}
