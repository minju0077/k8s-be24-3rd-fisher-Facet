package com.facet.api.auction.service;

import com.facet.api.auction.model.AucDto;
import com.facet.api.auction.model.AucProduct;
import com.facet.api.auction.model.Bid;
import com.facet.api.auction.repository.AuctionBidRepository;
import com.facet.api.auction.repository.AuctionReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuctionBidService {
    private final AuctionBidRepository auctionBidRepository;
    private final AuctionReadRepository auctionReadRepository;

    @Transactional
    public AucDto.BidRes bid(AucDto.BidReq dto) {
        Bid entity = auctionBidRepository.save(dto.toEntity());

        // 2. 상품(AucProduct)의 현재가 업데이트하기
        AucProduct product = auctionReadRepository.findById(dto.getAucProductIdx())
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        product.setCurrentPrice(dto.getBidPrice());

        return AucDto.BidRes.from(entity);
    }
}
