package com.facet.api.auction.service;

import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3ImageService implements UploadService{
    @Value("${spring.cloud.aws.s3.bucket}")
    private String s3BucketName;
    private final S3Operations s3Operations;

    public String saveFile(MultipartFile file) throws IOException {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String filePath = date + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        S3Resource s3Resource = s3Operations.upload(s3BucketName, filePath, file.getInputStream());
        // 업로드된 파일의 주소를 생성 (서울 리전 기준)
        // 형식: https://{버킷이름}.s3.{리전}.amazonaws.com/{파일경로}
        return s3Resource.getURL().toString();
    }

    @Override
    public List<String> upload(List<MultipartFile> fileList) {
        List<String> uploadPathList = new ArrayList<>();

            try {
                for(MultipartFile file : fileList){
                    String uploadPath = saveFile(file);
                    uploadPathList.add(uploadPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        return uploadPathList;
    }
}
