package com.facet.api.funding.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundMakerDto {

    private Long idx;
    private String experience;
    private String style;
    private String promise;
    private String makerStory;

    public static FundMaker from(FundMaker entity){
        return FundMaker.builder()
                .idx(entity.getIdx())
                .experience(entity.getExperience())
                .style(entity.getExperience())
                .promise(entity.getPromise())
                .build();
    }
}
