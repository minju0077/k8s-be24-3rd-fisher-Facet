package com.facet.api.funding.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;


@Getter
@Entity
public class FundingProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String img;

    private Long percent;

    @Column(nullable = false)
    private Long price;

    private Long targetPrice;  // 모인 금액

    private Long supporters; // 서포터즈

    @Column(nullable = false)
    private Long days; // 남은 일

    @Column(nullable = false)
    private String endDays; // 남은 날짜

    @Column(nullable = false)
    private String status;  // 상태

    @Column(nullable = false)
    @ColumnDefault(value="'FUNDING'")
    private String type;

    @OneToMany(mappedBy = "fundingProduct", fetch = FetchType.LAZY)
    List<FundingRewards> fundingRewardsList;

    @OneToOne(mappedBy = "fundingProduct", fetch = FetchType.LAZY)
    private FundingStory fundingStory;

    @OneToOne(mappedBy = "fundingProduct", fetch = FetchType.LAZY)
    private FundingMaker fundingMaker;

    @OneToMany(mappedBy = "fundingProduct", fetch = FetchType.LAZY)
    List<FundingProcess> fundingProcessList;

    @OneToMany(mappedBy = "fundingProduct",fetch = FetchType.LAZY)
    List<FundingImg> fundingImgList;

}
