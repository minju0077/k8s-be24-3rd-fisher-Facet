package com.facet.api.funding.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class FundProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_idx")
    private FundProduct fundProduct;
}
