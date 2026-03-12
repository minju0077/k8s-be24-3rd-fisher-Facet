package com.facet.api.auction.controller;

import com.facet.api.auction.service.AuctionReadService;
import com.facet.api.auction.model.AucDto;
import com.facet.api.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auction")
@RestController
@RequiredArgsConstructor
public class AuctionReadController {
    private final AuctionReadService auctionReadService;

    @GetMapping("/list")
    private ResponseEntity list(
            @RequestParam(required = true, defaultValue = "0") int page,
            @RequestParam(required = true, defaultValue = "12") int size
    ){
        AucDto.PageRes dto = auctionReadService.list(page, size);

        return ResponseEntity.ok(BaseResponse.success(dto)  );
    }

    @GetMapping("/search/{search}")
    public ResponseEntity search(@PathVariable String search){
        AucDto.PageRes dto = auctionReadService.search(0, 12, search);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/detail/{prodIdx}")
    public ResponseEntity detail(@PathVariable Long prodIdx){
        AucDto.DetailRes dto = auctionReadService.detail(prodIdx);
        return ResponseEntity.ok(BaseResponse.success(dto));
    }

}
