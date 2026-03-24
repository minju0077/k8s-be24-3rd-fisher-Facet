package com.facet.api.funding;

import com.facet.api.funding.model.FundProduct;
import com.facet.api.funding.model.FundDto;
import com.facet.api.funding.order.FundOrdersRepository;
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
    private final FundOrdersRepository fundOrdersRepository;

    // 메인 리스트 조회
    public  List<FundDto.FundingListRes> list() {
        List<FundProduct> res = fundingRepository.findAll();
        List<FundDto.FundingListRes> result = new ArrayList<>();

        for(FundProduct data: res){
            result.add(FundDto.FundingListRes.from(data));
        }

        return result;
    }

    // 리스트 페이지 리스트 조회
    public FundDto.PageRes pageList(int page, int size, String currentFilter, String categories, String currentStatus) {
        // 기본 정렬: 최신순(idx 오름차순으로 )
        Sort sort = Sort.by("idx").ascending();

        if ("percent".equals(currentFilter)) {
            sort = Sort.by("percent").descending(); // 낮은 인기순
        } else if ("supporters".equals(currentFilter)) {
            sort = Sort.by("supporters").descending(); // 마감 임박순 (필드명에 맞춰 수정 필요)
        }
        // 카테고리 -----------------------------------------------

        // 정렬 정보(sort)를 포함하여 PageRequest 생성
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<FundProduct> result;

        String status = currentStatus.toUpperCase();
        boolean isAllStatus = "ALL".equals(status);
        boolean isAllCategories = "all".equals(categories);

        // 3. 경우의 수에 따른 Repository 호출
        if (isAllCategories) {
            if (isAllStatus) {
                // 카테고리 전체 + 상태 전체 (LIVE + END 모두 나옴)
                result = fundingRepository.findAll(pageRequest);
            } else {
                // 카테고리 전체 + 특정 상태 (LIVE 또는 END)
                result = fundingRepository.findByStatus(status, pageRequest);
            }
        } else {
            if (isAllStatus) {
                // 특정 카테고리 + 상태 전체 (해당 카테고리의 LIVE + END 모두 나옴)
                result = fundingRepository.findByCategory(categories, pageRequest);
            } else {
                // 특정 카테고리 + 특정 상태
                result = fundingRepository.findByCategoryAndStatus(categories, status, pageRequest);
            }
        }

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
