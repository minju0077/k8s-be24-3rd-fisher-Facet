package com.facet.api.funding.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundingStory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String keyPoint;

    @Column(nullable = false)
    private String material;

    @Column(nullable = false)
    private String handMade;

    @Column(nullable = false)
    private String projectStory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_idx")
    private FundingProduct fundingProduct;

}
