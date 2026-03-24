package com.facet.api.funding.model;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

public class FundDto {



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
        private Long targetPrice;
        private Long supporters;
        private String endDays;
        private String status;

        public static FundDto.FundingListRes from(FundProduct entity){
            return FundingListRes.builder()
                    .idx(entity.getIdx())
                    .category(entity.getCategory())
                    .name(entity.getName())
                    .brand(entity.getBrand())
                    .img(entity.getImg())
                    .percent(entity.getPercent())
                    .supporters(entity.getSupporters())
                    .targetPrice(entity.getTargetPrice())
                    .endDays(entity.getEndDays())
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

        public static PageRes from(Page<FundProduct> entity){
            return PageRes.builder()
                    .fundingList(entity.get().map(FundDto.FundingListRes::from).toList())
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
        private Long targetPrice;   // 모인 금액
        private Long goalPrice;    // 목표 금액
        private Long supporters; // 서포터즈
        private String endDays;
        private String status;
        private String type;
        private List<FundRewardsDto> fundingRewardsList;
        private FundStory fundStory;
        private FundMaker fundMaker;
        private List<FundProcessDto> fundProcessList;
        private List<FundImgDto> fundImgList;


        public static DescListRes from(FundProduct entity){
            return DescListRes.builder()
                    .idx(entity.getIdx())
                    .category(entity.getCategory())
                    .name(entity.getName())
                    .brand(entity.getBrand())
                    .img(entity.getImg())
                    .percent(entity.getPercent())
                    .targetPrice(entity.getTargetPrice())
                    .supporters(entity.getSupporters())
                    .endDays(entity.getEndDays())
                    .endDays(entity.getEndDays())
                    .status(entity.getStatus())
                    .type(entity.getType())
                    .fundingRewardsList(entity.getFundRewardsList().stream().map(FundRewardsDto::from).toList())
                    .fundStory(entity.getFundStory() != null ? FundStoryDto.from(entity.getFundStory()) : null)
                    .fundMaker(entity.getFundMaker() != null ? FundMakerDto.from(entity.getFundMaker()) : null)
                    .fundProcessList(entity.getFundProcessList().stream().map(FundProcessDto::from).toList())
                    .fundImgList(entity.getFundImgList().stream().map(FundImgDto::from).toList())
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
        private int price;
        private Long targetPrice;  // 모인 금액
        private Long supporters; // 서포터즈
        private String endDays;
        private String status;
        private String type;

        public static DetailListRes from(FundProduct entity){
            return DetailListRes.builder()
                    .idx(entity.getIdx())
                    .category(entity.getCategory())
                    .name(entity.getName())
                    .brand(entity.getBrand())
                    .img(entity.getImg())
                    .percent(entity.getPercent())
                    .targetPrice(entity.getTargetPrice())
                    .supporters(entity.getSupporters())
                    .endDays(entity.getEndDays())
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

        public static DetailRes from(Page<FundProduct> entity){
            return DetailRes.builder()
                    .fundingList(entity.get().map(FundDto.FundingListRes::from).toList())
                    .totalPage(entity.getTotalPages())
                    .totalCount(entity.getTotalElements())
                    .currentPage(entity.getPageable().getPageNumber())
                    .currentSize(entity.getPageable().getPageSize())
                    .build();
        }
    }

}
