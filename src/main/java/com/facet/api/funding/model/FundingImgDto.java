package com.facet.api.funding.model;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundingImgDto {
    private Long idx;
    private String imgDetail;

    public static FundingImgDto from(FundingImg entity){
        return FundingImgDto.builder()
                .idx(entity.getIdx())
                .imgDetail(entity.getImgDetail())
                .build();
    }
}
