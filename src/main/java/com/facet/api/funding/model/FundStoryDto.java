package com.facet.api.funding.model;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundStoryDto {
    private Long idx;
    private String keyPoint;
    private String material;
    private String handMade;
    private String projectStory;

    public static FundStory from(FundStory entity){
        return FundStory.builder()
                .idx(entity.getIdx())
                .keyPoint(entity.getKeyPoint())
                .material(entity.getMaterial())
                .handMade(entity.getHandMade())
                .projectStory(entity.getProjectStory())
                .build();
    }
}
