package com.facet.api.auction.controller;

import com.facet.api.auction.model.AucDto;
import com.facet.api.auction.service.S3ImageService;
import com.facet.api.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/auction/image")
@RequiredArgsConstructor
@RestController
public class UploadController {
    private final S3ImageService s3ImageService;

    @GetMapping("/upload")
    public ResponseEntity upload(AucDto.ImageReq dto){
        List<String> result = s3ImageService.upload(dto);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/get/{productIdx}")
    public ResponseEntity getImage(@PathVariable Long productIdx){
        String imagePath = s3ImageService.getImage(productIdx);
        return ResponseEntity.ok(imagePath);
    }
}
