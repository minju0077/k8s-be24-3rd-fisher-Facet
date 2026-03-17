package com.facet.api.funding.model;

import jakarta.persistence.*;
import lombok.Getter;

import javax.annotation.processing.Generated;

@Getter
@Entity
public class FundingProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_idx")
    private FundingProduct fundingProduct;
}
