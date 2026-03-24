package com.facet.api.auction.controller;

import com.facet.api.auction.service.AuctionReadService;
import com.facet.api.auction.model.AucDto;
import com.facet.api.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auction")
@RestController
@RequiredArgsConstructor
@Tag(name = "경매 상품 리스트 조회 기능")
public class AuctionReadController {
    private final AuctionReadService auctionReadService;

    @GetMapping("/mainList")
    @Operation(summary = "경매 메인 페이지 리스트 조회 (페이징) 기능", description = "메인 화면에 노출될 기본적인 경매 상품 목록을 조회.")
    private ResponseEntity mainList(
            @RequestParam(required = true, defaultValue = "0") int page,
            @RequestParam(required = true, defaultValue = "10") int size
    ){
        AucDto.PageRes dto = auctionReadService.list(page, size);

        return ResponseEntity.ok(BaseResponse.success(dto)  );
    }
    @GetMapping("/list")
    @Operation(summary = "경매 전체 리스트 목록 조회 (페이징) 기능", description = "메인 화면에 노출될 기본적인 경매 상품 목록을 조회.")
    private ResponseEntity list(
            @RequestParam(required = true, defaultValue = "0") int page,
            @RequestParam(required = true, defaultValue = "12") int size
    ){
        AucDto.PageRes dto = auctionReadService.list(page, size);

        return ResponseEntity.ok(BaseResponse.success(dto)  );
    }

    @GetMapping("/search/{search}")
    @Operation(summary = "경매 상품 검색 기능", description = "경매 상품을 검색하여 해당하는 상품 정보 조회")
    public ResponseEntity search(@PathVariable String search){
        AucDto.PageRes dto = auctionReadService.search(0, 12, search);
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/detail/{prodIdx}")
    @Operation(summary = "경매 상품 상세 정보 조회 기능", description = "특정 경매 상품(prodIdx) 상세 정보 조회.")
    public ResponseEntity detail(@PathVariable Long prodIdx){
        AucDto.DetailRes dto = auctionReadService.detail(prodIdx);
        return ResponseEntity.ok(BaseResponse.success(dto));
    }

}
