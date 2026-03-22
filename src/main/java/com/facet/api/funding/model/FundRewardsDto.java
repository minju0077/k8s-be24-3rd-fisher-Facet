package com.facet.api.funding.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundRewardsDto {
    private Long idx;
    private String title;
    private String contents;
    private int price;
    private Integer quantity;  // 남은 수량
    private String tags; // 추천 배지

    public static FundRewardsDto from(FundRewards entity){
        return FundRewardsDto.builder()
                .idx(entity.getIdx())
                .title(entity.getTitle())
                .contents(entity.getContents())
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .tags(entity.getTags())
                .build();
    }
}
