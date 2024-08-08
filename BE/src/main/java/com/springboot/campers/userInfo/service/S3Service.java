package com.springboot.campers.userInfo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(S3Client s3Client, @Value("${aws.s3.bucketName}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file, String userId) throws IOException {
        // 사용자 ID와 고정 파일 이름을 결합하여 고유한 경로 생성
        String key = userId + "/profile.jpg";

        try {
            // 파일 업로드
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }

        return getUrlFromKey(key);
    }

    private String getUrlFromKey(String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }
}