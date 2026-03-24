package com.facet.api.auction.controller;

import com.facet.api.auction.model.AucDto;
import com.facet.api.auction.service.S3ImageService;
import com.facet.api.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/auction/image")
@RequiredArgsConstructor
@RestController
@Tag(name = "이미지 업로드 및 조회 기능")
public class UploadController {
    private final S3ImageService s3ImageService;

    @GetMapping("/upload")
    @Operation(summary = "경매 상품 이미지 S3 저장 ", description = "경매 상품 다중 이미지 S3에 저장 및 DB에 저장 ")
    public ResponseEntity upload(AucDto.ImageReq dto){
        List<String> result = s3ImageService.upload(dto);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/get/{productIdx}")
    @Operation(summary = "경매 상품 이미지 조회 기능 (S3)", description = "특정 경매 상품(prodIdx) 이미지 정보 조회.")
    public ResponseEntity getImage(@PathVariable Long productIdx){
        String imagePath = s3ImageService.getImage(productIdx);
        return ResponseEntity.ok(imagePath);
    }
}
