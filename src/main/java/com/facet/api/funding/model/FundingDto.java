package com.facet.api.funding.model;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

public class FundingDto {

    @Builder
    @Getter
    public static class FundingList{
        private Long idx;
        private String category;
        private String name;
        private String brand;
        private String img;
        private Long percent;
        private Long price;
        private Long supporters;
        private Long days;
        private String status;

        public static FundingDto.FundingList from(FundingProduct entity){
            return FundingList.builder()
                    .idx(entity.getIdx())
                    .category(entity.getCategory())
                    .name(entity.getName())
                    .brand(entity.getBrand())
                    .img(entity.getImg())
                    .percent(entity.getPercent())
                    .price(entity.getPrice())
                    .supporters(entity.getSupporters())
                    .days(entity.getDays())
                    .status(entity.getStatus())
                    .build();
        }
    }

    @Getter
    @Builder
    // 페이지 응답 dto 만들기
    public static class PageRes{
        private List<FundingList> fundingList;
        private int totalPage;
        private long totalCount;
        private int currentPage;
        private int currentSize;

        public static PageRes from(Page<FundingProduct> result){
            return PageRes.builder()
                    .fundingList(result.get().map(FundingDto.FundingList::from).toList())
                    .totalPage(result.getTotalPages())
                    .totalCount(result.getTotalElements())
                    .currentPage(result.getPageable().getPageNumber())
                    .currentSize(result.getPageable().getPageSize())
                    .build();
        }
    }
}
