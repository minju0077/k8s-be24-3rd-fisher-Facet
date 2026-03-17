package com.facet.api.funding.model;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FundingProcessDto {
    private Long idx;
    private String title;
    private String contents;

    public static FundingProcessDto from(FundingProcess entity){
        return FundingProcessDto.builder()
                .idx(entity.getIdx())
                .title(entity.getTitle())
                .contents(entity.getContents())
                .build();
    }
}
