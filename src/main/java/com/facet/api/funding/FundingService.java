package com.facet.api.funding;

import com.facet.api.funding.model.FundProduct;
import com.facet.api.funding.model.FundDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FundingService {
    private final FundingRepository fundingRepository;

    public  List<FundDto.FundingListRes> list() {
        List<FundProduct> res = fundingRepository.findAll();
        List<FundDto.FundingListRes> result = new ArrayList<>();

        for(FundProduct data: res){
            result.add(FundDto.FundingListRes.from(data));
        }

        return result;
    }

    public FundDto.PageRes pageList(int page, int size, String currentFilter, String categories) {
        // 기본 정렬: 최신순(idx 오름차순으로 )
        Sort sort = Sort.by("idx").ascending();
        String sortType = currentFilter;

        if ("price".equals(sortType)) {
            sort = Sort.by("price").ascending(); // 낮은 가격순
        } else if ("imminent".equals(sortType)) {
            sort = Sort.by("days").ascending(); // 마감 임박순 (필드명에 맞춰 수정 필요)
        }
        // 카테고리 -----------------------------------------------

        // 정렬 정보(sort)를 포함하여 PageRequest 생성
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        if(categories.equals("all")){
            Page<FundProduct> result = fundingRepository.findAll(pageRequest);
            return FundDto.PageRes.from(result);
        }
        Page<FundProduct> result = fundingRepository.findByCategory(categories,pageRequest);
        return FundDto.PageRes.from(result);
    }

    public FundDto.DescListRes descList(Long idx) {
        Optional<FundProduct> dto = fundingRepository.findById(idx);

        if(dto.isPresent()){
            FundProduct data = dto.get();
            return FundDto.DescListRes.from(data);
        }
        return null;
    }

    public FundDto.DetailRes detailList(int page, int size, int endDay) {
        Sort sort = Sort.by("endDays").ascending();  // day 기준으로 정렬
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<FundProduct> result = fundingRepository.findAll(pageRequest);
        // Page<FundingProduct> result = fundingRepository.findByDays(endDay,pageRequest);
        return FundDto.DetailRes.from(result);
    }
}
