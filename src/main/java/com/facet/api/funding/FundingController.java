package com.facet.api.funding;

import com.facet.api.common.model.BaseResponse;
import com.facet.api.funding.model.FundingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/funding")
@RequiredArgsConstructor
@RestController
public class FundingController {
    private final FundingService fundingService;

    @GetMapping("/fundinglist")
    public ResponseEntity list(){
        List<FundingDto.FundingListRes> result = fundingService.list();
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/fundingPageList")
    public ResponseEntity pageList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "all") String currentFilter,
            @RequestParam(defaultValue = "all") String currentCategories
    ){
        FundingDto.PageRes result = fundingService.pageList(page, size, currentFilter,currentCategories);

        return ResponseEntity.ok(BaseResponse.success(result));
    }


    @GetMapping("/descList/{idx}")
    public ResponseEntity descList(@PathVariable Long idx){
        FundingDto.DescListRes result = fundingService.descList(idx);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/DetailList")
    public ResponseEntity detailList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(defaultValue = "1") int endDay

    ){
        FundingDto.DetailRes result =  fundingService.detailList(page,size,endDay);
        return ResponseEntity.ok(BaseResponse.success(result));
    }
}
