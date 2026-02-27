package com.facet.api.auction;

import com.facet.api.auction.model.AucDto;
import com.facet.api.auction.model.AucProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/auction")
@RestController
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;

    @GetMapping("/list")
    private ResponseEntity list(){
        List<AucDto.MainRes> productList = auctionService.list();

        return ResponseEntity.ok(productList);
    }

}
