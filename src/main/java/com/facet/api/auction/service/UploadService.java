package com.facet.api.auction.service;

import com.facet.api.auction.model.AucDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {
    List<String> upload(AucDto.ImageReq dto);
}
