package com.codeit.donggrina.domain.ProfileImage.service;

import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import com.codeit.donggrina.domain.ProfileImage.repository.ProfileImageRepository;
import com.codeit.donggrina.domain.ProfileImage.util.S3Uploader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final ProfileImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    public List<Long> uploadImage(List<MultipartFile> files) {
        List<CompletableFuture<Long>> futures = files.stream()
            .map(file -> s3Uploader.upload(file)
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
}
