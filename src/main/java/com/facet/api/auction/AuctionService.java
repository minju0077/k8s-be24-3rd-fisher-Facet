package com.facet.api.auction;

import com.facet.api.auction.model.AucDto;
import com.facet.api.auction.model.AucProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public AucDto.PageRes list(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AucProduct> result = (Page<AucProduct>) auctionRepository.findAllByStatusLessThanEqual(1, pageRequest);

        return AucDto.PageRes.from(result);
    }

    public AucDto.PageRes read(int page, int size, String search){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AucProduct> result = (Page<AucProduct>) auctionRepository.findAllByStatusLessThanEqualAndNameContaining(1, search, pageRequest);

        return AucDto.PageRes.from(result);
    }
}
