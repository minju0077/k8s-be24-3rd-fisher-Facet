package com.facet.api.funding.model;

import jakarta.persistence.*;
import lombok.Getter;


@Getter
@Entity
public class FundingImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String imgDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_idx")
    private FundingProduct fundingProduct;

}
