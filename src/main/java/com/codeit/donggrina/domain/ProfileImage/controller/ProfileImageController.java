package com.codeit.donggrina.domain.ProfileImage.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.ProfileImage.service.ProfileImageService;
import java.util.List;
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
    public ApiResponse<List<Long>> uploadImage(
        @RequestParam List<MultipartFile> files) {
        List<Long> result = imageService.uploadImage(files);
        return ApiResponse.<List<Long>>builder()
            .code(HttpStatus.OK.value())
            .message("이미지 업로드 성공")
            .data(result)
            .build();
    }
}
