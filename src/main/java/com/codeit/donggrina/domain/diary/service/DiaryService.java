package com.codeit.donggrina.domain.diary.service;

import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import com.codeit.donggrina.domain.comment.dto.response.CommentFindResponse;
import com.codeit.donggrina.domain.comment.entity.Comment;
import com.codeit.donggrina.domain.diary.dto.request.DiaryCreateRequest;
import com.codeit.donggrina.domain.diary.dto.request.DiarySearchRequest;
import com.codeit.donggrina.domain.diary.dto.request.DiaryUpdateRequest;
import com.codeit.donggrina.domain.diary.dto.response.DiaryFindListResponse;
import com.codeit.donggrina.domain.diary.dto.response.DiaryFindResponse;
import com.codeit.donggrina.domain.diary.entity.Diary;
import com.codeit.donggrina.domain.diary.entity.DiaryImage;
import com.codeit.donggrina.domain.diary.entity.DiaryPet;
import com.codeit.donggrina.domain.diary.repository.DiaryImageRepository;
import com.codeit.donggrina.domain.diary.repository.DiaryRepository;
import com.codeit.donggrina.domain.group.entity.Group;
import com.codeit.donggrina.domain.heart.entity.Heart;
import com.codeit.donggrina.domain.heart.repository.HeartRepository;
import com.codeit.donggrina.domain.member.entity.Member;
import com.codeit.donggrina.domain.member.repository.MemberRepository;
import com.codeit.donggrina.domain.pet.entity.Pet;
import com.codeit.donggrina.domain.pet.repository.PetRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final int FIRST_IMAGE = 0;
    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final PetRepository petRepository;
    private final DiaryImageRepository diaryImageRepository;
    private final HeartRepository heartRepository;

    @Transactional
    public void createDiary(DiaryCreateRequest diaryCreateRequest, Long memberId) {
        Member currentMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        Optional.ofNullable(currentMember.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));

        List<DiaryImage> images = new ArrayList<>();
        if (diaryCreateRequest.images() != null) {
            images = diaryCreateRequest.images().stream()
                .map((imageId) ->
                    diaryImageRepository.findById(imageId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이미지입니다.")))
                .toList();
        }

        List<Pet> pets = diaryCreateRequest.pets().stream()
            .map((id) ->
                petRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 반려동물입니다."))
            )
            .toList();

        Diary diary = Diary.builder()
            .content(diaryCreateRequest.content())
            .weather(diaryCreateRequest.weather())
            .diaryImages(images)
            .member(currentMember)
            .group(currentMember.getGroup())
            .pets(pets)
            .date(diaryCreateRequest.date())
            .build();

        if (diaryCreateRequest.isShare()) {
            diary.shareToStory();
        }

        diaryRepository.save(diary);
    }

    @Transactional
    public void updateDiary(Long diaryId, DiaryUpdateRequest diaryUpdateRequest, Long memberId) {
        Diary targetDiary = diaryRepository.findById(diaryId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다."));

        Member currentMember = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        Optional.ofNullable(currentMember.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));

        if (!targetDiary.getMember().equals(currentMember)
            && !targetDiary.getGroup().getCreator().equals(currentMember.getUsername())) {

            throw new IllegalArgumentException("삭제가 불가능합니다.");
        }

        List<Pet> pets = diaryUpdateRequest.pets().stream()
            .map((id) ->
                petRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 반려동물입니다."))
            )
            .toList();

        List<DiaryImage> images = targetDiary.getDiaryImages();
        if (diaryUpdateRequest.images() != null) {
            images = diaryUpdateRequest.images().stream()
                .map((imageId) ->
                    diaryImageRepository.findById(imageId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다.")))
                .toList();
        }

        targetDiary.update(diaryUpdateRequest.content(), diaryUpdateRequest.weather(),
            diaryUpdateRequest.isShare(), diaryUpdateRequest.date(), pets, images);
    }

    @Transactional
    public void deleteDiary(Long diaryId, Long memberId) {
        Member currentMember = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        Diary targetDiary = diaryRepository.findById(diaryId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다."));

        if (targetDiary.getMember().equals(currentMember)
            || targetDiary.getGroup().getCreator().equals(currentMember.getUsername())) {

            diaryRepository.delete(targetDiary);
            return;
        }
        throw new IllegalArgumentException("삭제가 불가능합니다.");
    }

    public List<DiaryFindListResponse> findDiaries(Long memberId, LocalDate date) {
        Member currentMember = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        Group group = Optional.ofNullable(currentMember.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));

        List<Diary> foundDiaries = diaryRepository.findAllByDate(date, group);

        return foundDiaries.stream()
            .map(diary -> {
                List<String> petImages = diary.getDiaryPets().stream()
                    .map(diaryPet -> diaryPet.getPet().getProfileImage().getUrl())
                    .toList();

                String contentImage = null;
                if (!diary.getDiaryImages().isEmpty()) {
                    contentImage = diary.getDiaryImages().get(FIRST_IMAGE).getUrl();
                }

                int commentCount = getCommentCount(diary);

                Optional<Heart> favoriteOptional = heartRepository.findByMemberAndDiary(
                    currentMember, diary);

                return DiaryFindListResponse.builder()
                    .diaryId(diary.getId())
                    .authorImage(currentMember.getProfileImage().getUrl())
                    .author(currentMember.getNickname())
                    .petImages(petImages)
                    .content(diary.getContent())
                    .contentImage(contentImage)
                    .commentCount(commentCount)
                    .favoriteCount(diary.getHeartCount())
                    .favoriteState(favoriteOptional.isPresent())
                    .isMyDiary(diary.getMember().equals(currentMember)
                        || group.getCreator().equals(currentMember.getUsername()))
                    .build();
            })
            .toList();
    }

    public DiaryFindResponse findDiary(Long diaryId, Long memberId) {
        Diary foundDiary = diaryRepository.findByIdWithDetails(diaryId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다."));

        Member currentMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        Group foundDiaryGroup = foundDiary.getGroup();

        List<Pet> pets = foundDiary.getDiaryPets().stream()
            .map(DiaryPet::getPet)
            .toList();

        List<String> petImageUrls = pets.stream()
            .map(pet -> pet.getProfileImage().getUrl())
            .toList();

        List<String> contentImages = foundDiary.getDiaryImages().stream()
            .map(DiaryImage::getUrl)
            .distinct()
            .toList();

        Optional<Heart> favoriteOptional = heartRepository.findByMemberIdAndDiary(memberId,
            foundDiary);

        List<CommentFindResponse> comments = foundDiary.getComments().stream()
            .filter(comment -> comment.getParent() == null)
            .map(comment -> {

                List<CommentFindResponse> childrenResponse = new ArrayList<>();
                List<Comment> children = comment.getChildren();

                for (Comment child : children) {
                    Member commentAuthor = child.getMember();

                    childrenResponse.add(CommentFindResponse.builder()
                        .commentId(child.getId())
                        .commentAuthorImage(commentAuthor.getProfileImage().getUrl())
                        .commentAuthor(commentAuthor.getNickname())
                        .comment(child.getContent())
                        .date(child.getCreatedAt().toLocalDate())
                        .isMyComment(commentAuthor.equals(currentMember)
                            || foundDiaryGroup.getCreator().equals(currentMember.getUsername()))
                        .build());
                }
                Member commentAuthor = comment.getMember();

                return CommentFindResponse.builder()
                    .commentId(comment.getId())
                    .commentAuthorImage(commentAuthor.getProfileImage().getUrl())
                    .commentAuthor(commentAuthor.getNickname())
                    .comment(comment.getContent())
                    .date(comment.getCreatedAt().toLocalDate())
                    .isMyComment(commentAuthor.equals(currentMember)
                        || foundDiaryGroup.getCreator().equals(currentMember.getUsername()))
                    .children(childrenResponse)
                    .build();
            })
            .toList();

        Member author = foundDiary.getMember();

        return DiaryFindResponse.builder()
            .date(foundDiary.getDate())
            .authorImage(author.getProfileImage().getUrl())
            .author(author.getNickname())
            .petImages(petImageUrls)
            .contentImages(contentImages)
            .content(foundDiary.getContent())
            .weather(foundDiary.getWeather())
            .favoriteState(favoriteOptional.isPresent())
            .favoriteCount(foundDiary.getHeartCount())
            .comments(comments)
            .isMyDiary(author.equals(currentMember)
                || foundDiaryGroup.getCreator().equals(currentMember.getUsername()))
            .build();
    }

    public List<DiaryFindListResponse> searchDiaries(DiarySearchRequest diarySearchRequest,
        Long memberId) {
        Member currentMember = memberRepository.findByIdWithGroup(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        Optional.ofNullable(currentMember.getGroup())
            .orElseThrow(() -> new IllegalArgumentException("그룹에 속해 있지 않은 사용자입니다."));

        List<Diary> foundDiaries = diaryRepository.searchDiaries(diarySearchRequest,
            currentMember.getGroup());

        return foundDiaries.stream()
            .map(diary -> {
                List<Pet> pets = diary.getDiaryPets().stream()
                    .map(DiaryPet::getPet)
                    .toList();

                List<ProfileImage> petImages = pets.stream()
                    .map(Pet::getProfileImage)
                    .toList();

                List<String> imageUrls = petImages.stream()
                    .map(ProfileImage::getUrl)
                    .toList();

                String imageUrl = null;
                if (!diary.getDiaryImages().isEmpty()) {
                    imageUrl = diary.getDiaryImages().get(FIRST_IMAGE).getUrl();
                }

                int commentCount = getCommentCount(diary);

                Optional<Heart> favoriteOptional = heartRepository.findByMemberAndDiary(
                    currentMember, diary);

                return DiaryFindListResponse.builder()
                    .diaryId(diary.getId())
                    .authorImage(diary.getMember().getProfileImage().getUrl())
                    .author(diary.getMember().getNickname())
                    .petImages(imageUrls)
                    .contentImage(imageUrl)
                    .content(diary.getContent())
                    .commentCount(commentCount)
                    .favoriteCount(diary.getHeartCount())
                    .favoriteState(favoriteOptional.isPresent())
                    .isMyDiary(currentMember.getId().equals(diary.getMember().getId())
                        || currentMember.getGroup().getCreator()
                        .equals(currentMember.getUsername()))
                    .build();
            })
            .toList();
    }

    private int getCommentCount(Diary diary) {
        int commentCount = diary.getComments().size();
        List<Comment> comments = diary.getComments();
        for (Comment comment : comments) {
            commentCount += comment.getChildren().size();
        }

        return commentCount;
    }
}
