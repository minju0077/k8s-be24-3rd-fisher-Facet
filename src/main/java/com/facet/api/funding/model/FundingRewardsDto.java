package com.facet.api.funding.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundingRewardsDto {
    private Long idx;
    private String title;
    private String contents;
    private Long price;
    private Integer stock;  // 남은 수량
    private String tags; // 추천 배지

    public static FundingRewardsDto from(FundingRewards entity){
        return FundingRewardsDto.builder()
                .idx(entity.getIdx())
                .title(entity.getTitle())
                .contents(entity.getContents())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .tags(entity.getTags())
                .build();
    }
}
