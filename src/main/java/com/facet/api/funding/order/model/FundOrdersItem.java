package com.facet.api.funding.order.model;

import com.facet.api.funding.model.FundProduct;
import com.facet.api.funding.model.FundRewards;
import jakarta.persistence.*;
import lombok.*;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
public class FundOrdersItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;


    private Long productIdx;

    private int quantity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_idx")
    private FundOrders fundOrders;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_idx")
    private FundRewards fundRewards;

}
