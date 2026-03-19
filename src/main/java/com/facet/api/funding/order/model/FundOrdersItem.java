package com.facet.api.funding.order.model;

import com.facet.api.funding.model.FundProduct;
import com.facet.api.funding.model.FundRewards;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FundOrdersItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_idx")
    private FundOrders fundOrders;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_idx")
    private FundRewards fundRewards; // 구매한 특정 리워드 참조
}
