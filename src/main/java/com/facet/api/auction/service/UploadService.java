package com.facet.api.auction.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {
    List<String> upload(List<MultipartFile> fileList);
}
