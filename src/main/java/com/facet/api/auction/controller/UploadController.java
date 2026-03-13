package com.facet.api.auction.controller;

import com.facet.api.auction.service.S3ImageService;
import com.facet.api.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/upload")
@RequiredArgsConstructor
@RestController
public class UploadController {
    private final S3ImageService s3ImageService;

    @GetMapping("/image")
    public ResponseEntity upload(List<MultipartFile> images){
        List<String> result = s3ImageService.upload(images);
        return ResponseEntity.ok(BaseResponse.success(result));
    }
}
