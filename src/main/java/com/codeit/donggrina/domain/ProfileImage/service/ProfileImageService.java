package com.codeit.donggrina.domain.ProfileImage.service;

import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import com.codeit.donggrina.domain.ProfileImage.repository.ProfileImageRepository;
import com.codeit.donggrina.domain.ProfileImage.util.S3Handler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileImageService {

    private final ProfileImageRepository imageRepository;
    private final S3Handler s3Handler;

    public List<Long> uploadImage(List<MultipartFile> files) {
        List<CompletableFuture<Long>> futures = files.stream()
            .map(file -> s3Handler.upload(file)
                .thenApply(imageUrl -> {
                    ProfileImage profileImage = ProfileImage.builder()
                        .name(file.getOriginalFilename())
                        .url(imageUrl)
                        .build();
                    ProfileImage savedImage = imageRepository.save(profileImage);
                    return savedImage.getId();
                })
            ).toList();

        List<Long> ids = new ArrayList<>();
        futures.forEach(future -> ids.add(future.join()));

        return ids;
    }

    /**
     * 매일 자정에 S3와 DB에 저장된 프로필 이미지 중, Member 또는 Pet과 연결되지 않은 데이터 삭제
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteUnlinkedImageInDBAndS3() {
        List<Long> unlinkedProfileImageIds = imageRepository.findUnlinkedProfileImageIds();
        unlinkedProfileImageIds.forEach(id -> {
            ProfileImage profileImage = imageRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("프로필 이미지 스케줄링 삭제 중 오류, 존재하지 않는 id={}", id);
                    return new IllegalArgumentException("존재하지 않는 프로필 이미지입니다.");
                });
            String fileName = profileImage.getUrl().split("/")[3];
            CompletableFuture<Void> deleteFuture = s3Handler.delete(fileName);
            deleteFuture.thenRun(() -> imageRepository.delete(profileImage));
        });
    }
}
