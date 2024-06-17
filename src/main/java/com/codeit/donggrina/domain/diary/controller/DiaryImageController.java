package com.codeit.donggrina.domain.diary.controller;

import com.codeit.donggrina.common.api.ApiResponse;
import com.codeit.donggrina.domain.diary.service.DiaryImageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class DiaryImageController {

    private final DiaryImageService diaryImageService;

    @PostMapping("/images/diaries")
    public ApiResponse<List<Long>> uploadImages(@RequestParam List<MultipartFile> images) {
        return ApiResponse.<List<Long>>builder()
            .code(HttpStatus.CREATED.value())
            .message("다이어리 이미지 저장 성공")
            .data(diaryImageService.saveImage(images))
            .build();
    }
}
