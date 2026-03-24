package com.facet.api.funding;

import com.facet.api.common.model.BaseResponse;
import com.facet.api.funding.model.FundDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/funding")
@RequiredArgsConstructor
@RestController
@Tag(name = "펀딩 상품 리스트 조회 기능")
public class FundingController {
    private final FundingService fundingService;

    @GetMapping("/fundinglist")
    @Operation(summary = "경매 메인 페이지 리스트 조회 기능", description = "메인 화면에 노출될 기본적인 펀딩 상품 목록을 조회.")
    public ResponseEntity list(){
        List<FundDto.FundingListRes> result = fundingService.list();
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/fundingPageList")
    @Operation(summary = "펀딩 전체 리스트 목록 조회 (조건/페이징) 기능", description = "카테고리 및 정렬 필터 조건에 맞춰 펀딩 상품 목록을 페이징 처리하여 반환.")
    public ResponseEntity pageList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "all") String currentFilter,
            @RequestParam(defaultValue = "all") String currentCategories,
            @RequestParam(defaultValue = "live") String currentStatus
    ){
        FundDto.PageRes result = fundingService.pageList(page, size, currentFilter,currentCategories,currentStatus);

        return ResponseEntity.ok(BaseResponse.success(result));
    }


    @GetMapping("/descList/{idx}")
    @Operation(summary = "펀딩 상품 상세 정보 조회 기능", description = "특정 펀딩 상품(idx)의 상세 정보, 달성률, 리워드 목록 등을 조회.")
    public ResponseEntity descList(@PathVariable Long idx){
        FundDto.DescListRes result = fundingService.descList(idx);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/DetailList")
    @Operation(summary = "마감 임박 펀딩 리스트 조회 (조건/페이징) 기능", description = "마감일이 얼마 남지 않은 순으로 상품을 정렬하여 상품 조회.")
    public ResponseEntity detailList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(defaultValue = "1") int endDay

    ){
        FundDto.DetailRes result =  fundingService.detailList(page,size,endDay);
        return ResponseEntity.ok(BaseResponse.success(result));
    }
}
