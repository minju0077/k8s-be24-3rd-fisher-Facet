package com.facet.api.funding.order.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FundOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Setter
    private String status; // PAID, CANCELLED

    @Setter
    private String pgPaymentId;

    private Long ordersIdx;

    private Long price; // 결제 금액

    @OneToMany(mappedBy = "fundOrders",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<FundOrdersItem> ordersItems  = new ArrayList<>();

}
