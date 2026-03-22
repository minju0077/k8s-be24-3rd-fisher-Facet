package com.facet.api.funding.model;

import com.facet.api.funding.order.model.FundOrders;
import com.facet.api.funding.order.model.FundOrdersItem;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;


@Getter
@Entity
public class FundProduct {
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
    private int price;

    private Long targetPrice;  // 모인 금액

    private Long goalPrice; // 목표 금액

    private Long supporters; // 서포터즈


    @Column(nullable = false)
    private String endDays; // 남은 날짜

    @Column(nullable = false)
    private String status;  // 상태

    @Column(nullable = false)
    @ColumnDefault(value="'FUNDING'")
    private String type;

    @OneToMany(mappedBy = "fundProduct", fetch = FetchType.LAZY)
    List<FundRewards> fundRewardsList;

    @OneToOne(mappedBy = "fundProduct", fetch = FetchType.LAZY)
    private FundStory fundStory;

    @OneToOne(mappedBy = "fundProduct", fetch = FetchType.LAZY)
    private FundMaker fundMaker;

    @OneToMany(mappedBy = "fundProduct", fetch = FetchType.LAZY)
    private List<FundProcess> fundProcessList;

    @OneToMany(mappedBy = "fundProduct",fetch = FetchType.LAZY)
    private List<FundImg> fundImgList;

    @OneToMany(mappedBy = "fundProduct",fetch = FetchType.LAZY)
    private List<FundOrders> fundOrders;

}
