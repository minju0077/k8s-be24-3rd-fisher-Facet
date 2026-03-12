package com.facet.api.auction.controller;

import com.facet.api.auction.model.AucDto;
import com.facet.api.auction.service.AuctionBidService;
import com.facet.api.common.model.BaseEntity;
import com.facet.api.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auction")
@RequiredArgsConstructor
@RestController
public class AuctionBidController {
    private final AuctionBidService auctionBidService;


    @PostMapping("/bid")
    public ResponseEntity bid(@RequestBody AucDto.BidReq dto){
        AucDto.BidRes result = auctionBidService.bid(dto);
        return ResponseEntity.ok(BaseResponse.success(result));
    }
}
