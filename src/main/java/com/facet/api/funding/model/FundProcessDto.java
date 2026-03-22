package com.facet.api.funding.model;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FundProcessDto {
    private Long idx;
    private String title;
    private String contents;

    public static FundProcessDto from(FundProcess entity){
        return FundProcessDto.builder()
                .idx(entity.getIdx())
                .title(entity.getTitle())
                .contents(entity.getContents())
                .build();
    }
}
