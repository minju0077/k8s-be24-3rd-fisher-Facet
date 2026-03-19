package com.facet.api.funding.model;

import com.facet.api.funding.order.model.FundOrdersItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
public class FundRewards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private Integer stock;  // 남은 수량

    private String tags; // 추천 배지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_idx")
    private FundProduct fundProduct;

    @OneToMany(mappedBy = "fundRewards", fetch = FetchType.LAZY)
    List<FundOrdersItem> ordersItems;

}