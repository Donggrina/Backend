package com.codeit.donggrina.domain.comment.service;

import com.codeit.donggrina.domain.comment.dto.request.CommentAppendRequest;
import com.codeit.donggrina.domain.comment.entity.Comment;
import com.codeit.donggrina.domain.comment.repository.CommentRepository;
import com.codeit.donggrina.domain.diary.entity.Diary;
import com.codeit.donggrina.domain.diary.repository.DiaryRepository;
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
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Diary diary = diaryRepository.findById(diaryId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다."));

        // 댓글을 생성합니다.
        Comment comment = Comment.builder()
            .member(member)
            .diary(diary)
            .content(request.content())
            .build();

        // 부모 댓글이 있는 경우, 부모 댓글에 자식 댓글을 추가합니다.
        Optional.ofNullable(request.parentCommentId())
            .map(parentId -> commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부모 댓글입니다.")))
            .ifPresent(parentComment -> parentComment.addChildComment(comment));

        return commentRepository.save(comment).getId();
    }
}
