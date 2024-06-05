package com.codeit.donggrina.domain.diary.service;

import com.codeit.donggrina.domain.ProfileImage.util.S3Handler;
import com.codeit.donggrina.domain.diary.entity.DiaryImage;
import com.codeit.donggrina.domain.diary.repository.DiaryImageRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DiaryImageService {

    private final DiaryImageRepository diaryImageRepository;
    private final S3Handler s3Handler;

    @Transactional
    public List<Long> saveImage(List<MultipartFile> images) {
        List<CompletableFuture<Long>> futures = new ArrayList<>();
        for (MultipartFile image : images) {
            CompletableFuture<String> future = s3Handler.upload(image);

            futures.add(future.thenApply(imageUrl -> {
                DiaryImage diaryImage = DiaryImage.builder()
                    .name(image.getName())
                    .url(imageUrl)
                    .build();
                DiaryImage savedDiaryImage = diaryImageRepository.save(diaryImage);
                return savedDiaryImage.getId();
            }));
        }
        List<Long> imageIds = new ArrayList<>();
        futures.forEach(f -> imageIds.add(f.join()));

        return imageIds;
    }
}
