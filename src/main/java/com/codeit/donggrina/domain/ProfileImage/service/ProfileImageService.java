package com.codeit.donggrina.domain.ProfileImage.service;

import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import com.codeit.donggrina.domain.ProfileImage.repository.ProfileImageRepository;
import com.codeit.donggrina.domain.ProfileImage.util.S3Uploader;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final ProfileImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    public void uploadImage(MultipartFile file) {
        // 비동기 이미지 업로드 처리
        CompletableFuture<String> imageUrlFuture = s3Uploader.upload(file);

        // 이미지 업로드 후 future에 저장된 imageUrl 활용해서 ProfileImage 객체 생성 후 저장
        imageUrlFuture.thenAccept(imageUrl -> {
            ProfileImage profileImage = ProfileImage.builder()
                .name(file.getOriginalFilename())
                .url(imageUrl)
                .build();
            imageRepository.save(profileImage);
        });
    }
}
