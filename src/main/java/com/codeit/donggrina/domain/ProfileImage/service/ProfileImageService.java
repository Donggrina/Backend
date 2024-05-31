package com.codeit.donggrina.domain.ProfileImage.service;

import com.codeit.donggrina.domain.ProfileImage.entity.ProfileImage;
import com.codeit.donggrina.domain.ProfileImage.repository.ProfileImageRepository;
import com.codeit.donggrina.domain.ProfileImage.util.S3Uploader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final ProfileImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    public void uploadImage(List<MultipartFile> files) {
        files.forEach(file -> s3Uploader.upload(file)
                .thenAccept(imageUrl -> {
                    ProfileImage profileImage = ProfileImage.builder()
                        .name(file.getOriginalFilename())
                        .url(imageUrl)
                        .build();
                    imageRepository.save(profileImage);
                })
            );
    }
}
