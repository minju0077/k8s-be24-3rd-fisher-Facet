package com.facet.api.auction.service;

import com.facet.api.auction.model.AucDto;
import com.facet.api.auction.model.AucProdImage;
import com.facet.api.auction.repository.AuctionImageRepository;
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
    private final AuctionImageRepository auctionImageRepository;

    public String saveFile(Long productIdx,MultipartFile file) throws IOException {
        String filePath = productIdx + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        S3Resource s3Resource = s3Operations.upload(s3BucketName, filePath, file.getInputStream());
        // 업로드된 파일의 주소를 생성 (서울 리전 기준)
        // 형식: https://{버킷이름}.s3.{리전}.amazonaws.com/{파일경로}
        return s3Resource.getURL().toString();
    }

    @Override
    public List<String> upload(AucDto.ImageReq dto) {
        List<String> uploadPathList = new ArrayList<>();
        List<AucDto.ImagePathDto> imagePathDtoList = new ArrayList<>();
            try {
                for(MultipartFile file : dto.getFileList()){
                    String uploadPath = saveFile(dto.getProductIdx(), file);
                    AucDto.ImagePathDto imagePathDto = AucDto.ImagePathDto.builder()
                            .productIdx(dto.getProductIdx())
                            .imagePath(uploadPath)
                            .build();
                    AucProdImage entity = AucDto.ImagePathDto.toEntity(imagePathDto);
                    auctionImageRepository.save(entity);
                    uploadPathList.add(imagePathDto.getImagePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        return uploadPathList;
    }

    public String getImage(Long productIdx){
        AucProdImage entity = auctionImageRepository.findByAucProductIdx(productIdx);
        String imagePath = entity.getImagePath();

        return imagePath;
    }
}
