package com.facet.api.funding.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundingMaker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String experience;

    @Column(nullable = false)
    private String style;

    @Column(nullable = false)
    private String promise;

    @Column(nullable = false)
    private String makerStory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_idx")
    private FundingProduct fundingProduct;


}
