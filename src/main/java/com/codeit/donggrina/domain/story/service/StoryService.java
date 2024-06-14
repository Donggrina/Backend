package com.codeit.donggrina.domain.story.service;

import com.codeit.donggrina.domain.comment.dto.response.CommentFindResponse;
import com.codeit.donggrina.domain.comment.entity.Comment;
import com.codeit.donggrina.domain.diary.entity.Diary;
import com.codeit.donggrina.domain.diary.entity.DiaryImage;
import com.codeit.donggrina.domain.diary.repository.DiaryRepository;
import com.codeit.donggrina.domain.heart.entity.Heart;
import com.codeit.donggrina.domain.heart.repository.HeartRepository;
import com.codeit.donggrina.domain.member.dto.request.CustomOAuth2User;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import com.codeit.donggrina.domain.story.dto.response.StoryFindResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;

    @Transactional
    public void createStory(Long diaryId, Long memberId) {
        Diary targetDiary = diaryRepository.findByIdWithMember(diaryId)
            .orElseThrow(RuntimeException::new);

        if(!targetDiary.getMember().getId().equals(memberId)) {
            throw new RuntimeException();
        }

        targetDiary.shareToStory();
    }

    @Transactional
    public void deleteStory(Long diaryId, Long memberId) {
        Diary targetDiary = diaryRepository.findByIdWithMember(diaryId)
            .orElseThrow(RuntimeException::new);

        if(!targetDiary.getMember().getId().equals(memberId)) {
            throw new RuntimeException();
        }

        targetDiary.unShareToStory();
    }

    public StoryFindResponse findStory(Long diaryId, Long memberId) {
        Member currentMember = memberRepository.findById(memberId)
            .orElseThrow(RuntimeException::new);

        Diary foundStory = diaryRepository.findStoryWithDetails(diaryId)
            .orElseThrow(RuntimeException::new);

        List<String> images = foundStory.getDiaryImages().stream()
            .map(DiaryImage::getUrl)
            .toList();

        Optional<Heart> heartOptional = heartRepository.findByMemberAndDiary(currentMember,
            foundStory);

        List<CommentFindResponse> comments = new ArrayList<>();

        if(foundStory.getComments() != null) {
            comments = foundStory.getComments().stream()
                .map(comment -> {

                    List<CommentFindResponse> children = new ArrayList<>();
                    for(Comment child : comment.getChildren()) {
                        Member commentAuthor = child.getMember();

                        children.add(CommentFindResponse.builder()
                            .commentId(child.getId())
                            .commentAuthorImage(commentAuthor.getProfileImage().getUrl())
                            .commentAuthor(commentAuthor.getName())
                            .comment(child.getContent())
                            .date(child.getDate())
                            .build());
                    }

                    Member commentAuthor = comment.getMember();
                    return CommentFindResponse.builder()
                        .commentId(comment.getId())
                        .commentAuthorImage(commentAuthor.getProfileImage().getUrl())
                        .commentAuthor(commentAuthor.getName())
                        .comment(comment.getContent())
                        .date(comment.getDate())
                        .children(children)
                        .build();
                })
                .toList();
        }

        Member author = foundStory.getMember();

        return StoryFindResponse.builder()
            .authorImage(author.getProfileImage().getUrl())
            .authorGroup(author.getGroup().getName())
            .images(images)
            .content(foundStory.getContent())
            .createdDate(foundStory.getCreatedAt())
            .favoriteState(heartOptional.isPresent())
            .favoriteCount(foundStory.getHeartCount())
            .comments(comments)
            .build();
    }
}
