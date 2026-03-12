package com.facet.api.auction.service;

import com.facet.api.auction.model.AucDto;
import com.facet.api.auction.model.Bid;
import com.facet.api.auction.repository.AuctionBidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuctionBidService {
    private final AuctionBidRepository auctionBidRepository;

    public AucDto.BidRes bid(AucDto.BidReq dto) {
        Bid entity = auctionBidRepository.save(dto.toEntity());

        return AucDto.BidRes.from(entity);
    }
}
