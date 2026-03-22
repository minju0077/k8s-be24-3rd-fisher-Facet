package com.facet.api.funding;

import com.facet.api.funding.model.FundProduct;
import com.facet.api.funding.model.FundDto;
import com.facet.api.funding.order.FundOrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FundingService {
    private final FundingRepository fundingRepository;
    private final FundOrdersRepository fundOrdersRepository;

    // 메인 리스트 조회
    public  List<FundDto.FundListRes> list() {
        List<FundProduct> res = fundingRepository.findAll();
        List<FundDto.FundListRes> result = new ArrayList<>();

        for(FundProduct data: res){
            // 총 금액 합산 결과
            Long totalAmount = fundOrdersRepository.sumPaidPriceByProductIdx(data.getIdx());
            // 총 서포터 합산 결과
            Long totalSupporters = fundOrdersRepository.countPaidSupportersByProductIdx(data.getIdx());

            // null 처리 (주문이 하나도 없을 경우)
            totalAmount = (totalAmount != null) ? totalAmount : 0L;
            totalSupporters = (totalSupporters != null) ? totalSupporters : 0L;

            // 2. 퍼센트 계산: (현재금액 * 100) / 목표금액
            Long currentPercent = 0L;
            if (data.getGoalPrice() != null && data.getGoalPrice() > 0) {
                currentPercent = (totalAmount * 100) / data.getGoalPrice();
            }

            result.add(FundDto.FundListRes.from(data,totalAmount,totalSupporters,currentPercent ));
        }

        return result;
    }

    // 리스트 페이지 리스트 조회
    public FundDto.FundPageRes pageList(int page, int size, String currentFilter, String categories) {
        // 기본 정렬: 최신순(idx 오름차순으로 )
        Sort sort = Sort.by("idx").ascending();
        String sortType = currentFilter;

        if ("price".equals(sortType)) {
            sort = Sort.by("price").ascending(); // 낮은 가격순
        } else if ("imminent".equals(sortType)) {
            sort = Sort.by("endDays").ascending(); // 마감 임박순 (필드명에 맞춰 수정 필요)
        }
        // 카테고리 -----------------------------------------------

        // 정렬 정보(sort)를 포함하여 PageRequest 생성
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<FundProduct> result;
        if(categories.equals("all")){
            result = fundingRepository.findAll(pageRequest);
            List<FundDto.FundListRes> dtoList =  convertToDtoList(result.getContent());
            return FundDto.FundPageRes.from(result,dtoList);
        }
        result = fundingRepository.findByCategory(categories,pageRequest);
        List<FundDto.FundListRes> dtoList =  convertToDtoList(result.getContent());

        return FundDto.FundPageRes.from(result,dtoList);
    }

    public FundDto.DescListRes descList(Long idx) {
        Optional<FundProduct> dto = fundingRepository.findById(idx);

        if(dto.isPresent()){
            FundProduct data = dto.get();


            Long totalAmount = fundOrdersRepository.sumPaidPriceByProductIdx(data.getIdx());
            Long totalSupporters = fundOrdersRepository.countPaidSupportersByProductIdx(data.getIdx());

            totalAmount = (totalAmount != null) ? totalAmount : 0L;
            totalSupporters = (totalSupporters != null) ? totalSupporters : 0L;

            Long currentPercent = 0L;
            if (data.getGoalPrice() != null && data.getGoalPrice() > 0) {
                currentPercent = (totalAmount * 100) / data.getGoalPrice();
            }

            return FundDto.DescListRes.from(data,totalAmount ,totalSupporters,currentPercent);
        }
        return null;
    }

    public FundDto.DetailRes detailList(int page, int size, int endDay) {
        Sort sort = Sort.by("endDays").ascending();  // day 기준으로 정렬
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<FundProduct> result = fundingRepository.findAll(pageRequest);
        List<FundDto.FundListRes> dtoList = convertToDtoList(result.getContent());

        return FundDto.DetailRes.from(result,dtoList);
    }


    // 서포터즈, 모인 금액, 페센트 계산 메소드
    private List<FundDto.FundListRes> convertToDtoList(List<FundProduct> products) {
        List<FundDto.FundListRes> dtoList = new ArrayList<>();
        for (FundProduct data : products) {
            Long totalAmount = fundOrdersRepository.sumPaidPriceByProductIdx(data.getIdx());
            Long totalSupporters = fundOrdersRepository.countPaidSupportersByProductIdx(data.getIdx());

            totalAmount = (totalAmount != null) ? totalAmount : 0L;
            totalSupporters = (totalSupporters != null) ? totalSupporters : 0L;

            Long currentPercent = (data.getGoalPrice() != null && data.getGoalPrice() > 0)
                    ? (totalAmount * 100) / data.getGoalPrice() : 0L;

            dtoList.add(FundDto.FundListRes.from(data, totalAmount, totalSupporters, currentPercent));
        }
        return dtoList;
    }
}
