package com.codeit.donggrina.domain.ProfileImage.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.ProfileImage.service.ProfileImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ProfileImageController {

    private final ProfileImageService imageService;

    @PostMapping("/images")
    public ApiResponse<String> uploadImage(@RequestParam MultipartFile file) {
        imageService.uploadImage(file);
        return ApiResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message("이미지 업로드 성공")
            .build();
    }
}
