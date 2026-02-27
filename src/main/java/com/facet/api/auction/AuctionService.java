package com.facet.api.auction;

import com.facet.api.auction.model.AucDto;
import com.facet.api.auction.model.AucProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public List<AucDto.MainRes> list() {
        List<AucProduct> aucProductList = auctionRepository.findAllByStatusLessThanEqual(1);
        List<AucDto.MainRes> mainAucProduct = aucProductList.stream().map(AucProduct::from).toList();
        return mainAucProduct;
    }
}
