package com.facet.api.funding.order.model;


import com.facet.api.user.model.User;
import lombok.*;

import java.util.List;

public class FundOrdersDto {
    @Builder
    @Getter
    public static class OrdersItemReq {
        private Long productIdx;
        private int quantity;

        public FundOrdersItem toEntity(FundOrders orders) {
            return FundOrdersItem.builder()
                    .productIdx(this.productIdx)
                    .quantity(this.quantity)
                    .fundOrders(orders)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class OrdersItemRes {
        private Long idx;
        private Long productIdx;
        private int quantity;

        public static OrdersItemRes from(FundOrdersItem entity) {
            return OrdersItemRes.builder()
                    .idx(entity.getIdx())
                    .productIdx(entity.getProductIdx())
                    .quantity(entity.getQuantity())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class OrdersReq {
        private Long price;
        private List<OrdersItemReq> ordersItems;

        public FundOrders toEntity(User user) {
            FundOrders orders = FundOrders.builder()
                    .ordersIdx(user.getIdx())
                    .status("PENDING")
                    .price(this.price)
                    .build();

            if (this.ordersItems != null) {
                this.ordersItems.forEach(item -> {
                    orders.getOrdersItems().add(FundOrdersItem.builder()
                            .productIdx(item.getProductIdx())
                            .quantity(item.getQuantity())
                            .fundOrders(orders)
                            .build());
                });
            }

            return orders;
        }
    }


    @Builder
    @Getter
    public static class OrdersRes {
        private Long idx;
        private Long price;
        private String status;
        private List<OrdersItemRes> ordersItems;

        public static OrdersRes from(FundOrders entity) {
            return OrdersRes.builder()
                    .idx(entity.getIdx())
                    .status(entity.getStatus())
                    .price(entity.getPrice())
                    .ordersItems(entity.getOrdersItems().stream().map(OrdersItemRes::from).toList())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerifyReq {
        private String paymentId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerifyRes {
        private Long ordersIdx;

    }

}