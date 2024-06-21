package com.codeit.donggrina.domain.story.service;

import com.codeit.donggrina.domain.comment.dto.response.CommentFindResponse;
import com.codeit.donggrina.domain.comment.entity.Comment;
import com.codeit.donggrina.domain.diary.entity.Diary;
import com.codeit.donggrina.domain.diary.entity.DiaryImage;
import com.codeit.donggrina.domain.diary.repository.DiaryRepository;
import com.codeit.donggrina.domain.heart.entity.Heart;
import com.codeit.donggrina.domain.heart.repository.HeartRepository;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import com.codeit.donggrina.domain.story.dto.response.StoryFindListPage;
import com.codeit.donggrina.domain.story.dto.response.StoryFindListResponse;
import com.codeit.donggrina.domain.story.dto.response.StoryFindResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다."));
        Member currentMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        if (!targetDiary.getMember().getId().equals(memberId) && !targetDiary.getGroup()
            .getCreator().equals(currentMember.getUsername())) {

            throw new IllegalArgumentException("스토리 생성이 불가능합니다.");
        }

        targetDiary.shareToStory();
    }

    @Transactional
    public void deleteStory(Long diaryId, Long memberId) {
        Diary targetDiary = diaryRepository.findByIdWithMember(diaryId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다."));
        Member currentMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        if (!targetDiary.getMember().getId().equals(memberId) && !targetDiary.getGroup()
            .getCreator().equals(currentMember.getUsername())) {

            throw new IllegalArgumentException("스토리 생성이 불가능합니다.");
        }

        targetDiary.unShareToStory();
    }

    public StoryFindResponse findStory(Long diaryId, Long memberId) {
        Member currentMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        Diary foundStory = diaryRepository.findByIdWithMember(diaryId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다."));

        List<String> images = foundStory.getDiaryImages().stream()
            .map(DiaryImage::getUrl)
            .toList();

        Optional<Heart> heartOptional = heartRepository.findByMemberAndDiary(currentMember,
            foundStory);

        List<CommentFindResponse> comments = new ArrayList<>();

        if (foundStory.getComments() != null) {
            comments = foundStory.getComments().stream()
                .map(comment -> {

                    List<CommentFindResponse> children = new ArrayList<>();
                    for (Comment child : comment.getChildren()) {
                        Member commentAuthor = child.getMember();

                        children.add(CommentFindResponse.builder()
                            .commentId(child.getId())
                            .commentAuthorImage(commentAuthor.getProfileImage().getUrl())
                            .commentAuthor(commentAuthor.getName())
                            .comment(child.getContent())
                            .date(child.getDate())
                            .isMyComment(
                                commentAuthor.equals(currentMember) || commentAuthor.getGroup()
                                    .getCreator().equals(currentMember.getUsername()))
                            .build());
                    }

                    Member commentAuthor = comment.getMember();
                    return CommentFindResponse.builder()
                        .commentId(comment.getId())
                        .commentAuthorImage(commentAuthor.getProfileImage().getUrl())
                        .commentAuthor(commentAuthor.getName())
                        .comment(comment.getContent())
                        .date(comment.getDate())
                        .isMyComment(
                            commentAuthor.equals(currentMember) || commentAuthor.getGroup()
                                .getCreator().equals(currentMember.getUsername()))
                        .children(children)
                        .build();
                })
                .toList();
        }

        List<String> petImages = foundStory.getDiaryPets().stream()
            .map(diaryPet -> diaryPet.getPet().getProfileImage().getUrl())
            .toList();

        Member author = foundStory.getMember();

        return StoryFindResponse.builder()
            .authorImage(author.getProfileImage().getUrl())
            .author(author.getName())
            .petImages(petImages)
            .weather(foundStory.getWeather())
            .authorGroup(author.getGroup().getName())
            .images(images)
            .content(foundStory.getContent())
            .date(foundStory.getDate())
            .isMyStory(foundStory.getMember().equals(currentMember)
                || foundStory.getGroup().getCreator().equals(currentMember.getUsername()))
            .favoriteState(heartOptional.isPresent())
            .favoriteCount(foundStory.getHeartCount())
            .comments(comments)
            .build();
    }

    public StoryFindListPage findStories(Long memberId, Pageable pageable) {
        Member currentMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        Slice<Diary> page = diaryRepository.findPage(pageable);
        List<StoryFindListResponse> response = page.stream()
            .map(diary -> {

                Optional<Heart> heartOptional = heartRepository.findByMemberAndDiary(currentMember,
                    diary);

                List<String> images = diary.getDiaryImages().stream()
                    .map(DiaryImage::getUrl)
                    .toList();

                int commentCount = diary.getComments().size();
                for (Comment comment : diary.getComments()) {
                    commentCount += comment.getChildren().size();
                }

                List<String> petImages = diary.getDiaryPets().stream()
                    .map(diaryPet -> diaryPet.getPet().getProfileImage().getUrl())
                    .toList();

                Member author = diary.getMember();

                return StoryFindListResponse.builder()
                    .diaryId(diary.getId())
                    .authorImage(author.getProfileImage().getUrl())
                    .author(author.getName())
                    .petImages(petImages)
                    .weather(diary.getWeather())
                    .authorGroup(author.getGroup().getName())
                    .images(images)
                    .content(diary.getContent())
                    .commentCount(commentCount)
                    .favoriteCount(diary.getHeartCount())
                    .favoriteState(heartOptional.isPresent())
                    .date(diary.getDate())
                    .isMyStory(author.equals(currentMember)
                        || author.getGroup().getCreator().equals(currentMember.getUsername()))
                    .build();
            })
            .toList();

        return StoryFindListPage.builder()
            .response(response)
            .currentPage(page.getNumber())
            .hasMore(page.hasNext())
            .build();
    }
}
