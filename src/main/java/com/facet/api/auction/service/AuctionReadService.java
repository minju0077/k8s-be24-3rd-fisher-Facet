package com.facet.api.auction.service;

import com.facet.api.auction.model.AucDto;
import com.facet.api.auction.model.AucProduct;
import com.facet.api.auction.repository.AuctionReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuctionReadService {
    private final AuctionReadRepository auctionReadRepository;

    public AucDto.PageRes list(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AucProduct> resEntity = auctionReadRepository.findAll(pageRequest);
        return AucDto.PageRes.from(resEntity);
    }

    public AucDto.PageRes search(int page, int size, String search){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AucProduct> result = (Page<AucProduct>) auctionReadRepository.findAllByName(search, pageRequest);

        return AucDto.PageRes.from(result);
    }

    public AucDto.DetailRes detail(Long prodIdx) {
        AucProduct entity = auctionReadRepository.findById(prodIdx).orElseThrow();
        return AucDto.DetailRes.from(entity);
    }
}
