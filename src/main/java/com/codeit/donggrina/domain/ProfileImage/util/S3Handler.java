package com.codeit.donggrina.domain.ProfileImage.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.codeit.donggrina.domain.ProfileImage.exception.ImageDeleteException;
import com.codeit.donggrina.domain.ProfileImage.exception.ImageUploadException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Handler {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Async("imageUploadExecutor")
    public CompletableFuture<String> upload(MultipartFile file) {
        CompletableFuture<String> future = new CompletableFuture<>();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            // S3에 파일 업로드
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "-" + file.getOriginalFilename(); // 파일명 중복시 S3 객체 덮어쓰기 방지
            amazonS3Client.putObject(bucket, fileName, inputStream, metadata);

            // S3에 저장된 파일 이미지 URL 불러와서 future에 저장
            String imageUrl = amazonS3Client.getUrl(bucket, fileName).toString();
            future.complete(imageUrl);
        } catch (IOException e) {
            future.completeExceptionally(new ImageUploadException("이미지 업로드 중 오류가 발생했습니다.", e));
        }

        return future;
    }

    @Async("imageUploadExecutor")
    public CompletableFuture<Void> delete(String fileName) {
        return CompletableFuture
            .runAsync(() -> {
                amazonS3Client.deleteObject(bucket, fileName);
                log.info("S3 이미지 삭제 완료, fileName={}", fileName);
            })
            .exceptionally(e -> {
                log.error("S3에서 이미지 삭제 중 오류 발생, fileName={}", fileName, e);
                throw new ImageDeleteException("S3 이미지 삭제 중 오류가 발생했습니다.", e);
            });
    }
}
