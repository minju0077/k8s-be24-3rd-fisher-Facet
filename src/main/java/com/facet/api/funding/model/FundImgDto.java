package com.facet.api.funding.model;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundImgDto {
    private Long idx;
    private String imgDetail;

    public static FundImgDto from(FundImg entity){
        return FundImgDto.builder()
                .idx(entity.getIdx())
                .imgDetail(entity.getImgDetail())
                .build();
    }
}
