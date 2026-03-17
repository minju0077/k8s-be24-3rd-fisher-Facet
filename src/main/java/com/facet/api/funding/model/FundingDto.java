package com.facet.api.funding.model;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

public class FundingDto {

    @Builder
    @Getter
    // 펀딩 리스트 조회 응답
    public static class FundingListRes{
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

        public static FundingDto.FundingListRes from(FundingProduct entity){
            return FundingListRes.builder()
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
    // 펀딩 페이지 리스트 응답
    public static class PageRes{
        private List<FundingListRes> fundingList;
        private int totalPage;
        private long totalCount;
        private int currentPage;
        private int currentSize;

        public static PageRes from(Page<FundingProduct> entity){
            return PageRes.builder()
                    .fundingList(entity.get().map(FundingDto.FundingListRes::from).toList())
                    .totalPage(entity.getTotalPages())
                    .totalCount(entity.getTotalElements())
                    .currentPage(entity.getPageable().getPageNumber())
                    .currentSize(entity.getPageable().getPageSize())
                    .build();
        }
    }

    @Getter
    @Builder
    // 펀딩 상세 페이지 리스트 응답
    public static class DescListRes{
        private Long idx;
        private String category;
        private String name;
        private String brand;
        private String img;
        private Long percent;
        private Long price;
        private Long targetPrice;  // 모인 금액
        private Long supporters; // 서포터즈
        private Long days;
        private String endDays;
        private String status;
        private String type;
        private List<FundingRewardsDto> fundingRewardsList;
        private FundingStory fundingStory;
        private FundingMaker fundingMaker;
        private List<FundingProcessDto> fundingProcessList;
        private List<FundingImgDto> fundingImgList;

        public static DescListRes from(FundingProduct entity){
            return DescListRes.builder()
                    .idx(entity.getIdx())
                    .category(entity.getCategory())
                    .name(entity.getName())
                    .brand(entity.getBrand())
                    .img(entity.getImg())
                    .percent(entity.getPercent())
                    .price(entity.getPrice())
                    .targetPrice(entity.getTargetPrice())
                    .supporters(entity.getSupporters())
                    .days(entity.getDays())
                    .endDays(entity.getEndDays())
                    .status(entity.getStatus())
                    .type(entity.getType())
                    .fundingRewardsList(entity.getFundingRewardsList().stream().map(FundingRewardsDto::from).toList())
                    .fundingStory(entity.getFundingStory() != null ? FundingStoryDto.from(entity.getFundingStory()) : null)
                    .fundingMaker(entity.getFundingMaker() != null ? FundingMakerDto.from(entity.getFundingMaker()) : null)
                    .fundingProcessList(entity.getFundingProcessList().stream().map(FundingProcessDto::from).toList())
                    .fundingImgList(entity.getFundingImgList().stream().map(FundingImgDto::from).toList())
                    .build();
        }
    }

    @Getter
    @Builder
    // 펀딩 상세 페이지 리스트 응답
    public static class DetailListRes{
        private Long idx;
        private String category;
        private String name;
        private String brand;
        private String img;
        private Long percent;
        private Long price;
        private Long targetPrice;  // 모인 금액
        private Long supporters; // 서포터즈
        private Long days;
        private String status;
        private String type;

        public static DetailListRes from(FundingProduct entity){
            return DetailListRes.builder()
                    .idx(entity.getIdx())
                    .category(entity.getCategory())
                    .name(entity.getName())
                    .brand(entity.getBrand())
                    .img(entity.getImg())
                    .percent(entity.getPercent())
                    .price(entity.getPrice())
                    .targetPrice(entity.getTargetPrice())
                    .supporters(entity.getSupporters())
                    .days(entity.getDays())
                    .status(entity.getStatus())
                    .type(entity.getType())
                    .build();
        }
    }

    @Getter
    @Builder
    // 펀딩 페이지 리스트 응답
    public static class DetailRes{
        private List<FundingListRes> fundingList;
        private int totalPage;
        private long totalCount;
        private int currentPage;
        private int currentSize;

        public static DetailRes from(Page<FundingProduct> entity){
            return DetailRes.builder()
                    .fundingList(entity.get().map(FundingDto.FundingListRes::from).toList())
                    .totalPage(entity.getTotalPages())
                    .totalCount(entity.getTotalElements())
                    .currentPage(entity.getPageable().getPageNumber())
                    .currentSize(entity.getPageable().getPageSize())
                    .build();
        }
    }

}
