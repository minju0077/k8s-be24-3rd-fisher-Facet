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
public class FundOrdersItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private Long productIdx;
    private int quantity;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_idx")
    private FundOrders fundOrders;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_idx")
    private FundRewards fundRewards;

}
