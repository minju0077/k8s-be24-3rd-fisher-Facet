package com.facet.api.funding.order.model;


import com.facet.api.funding.model.FundProduct;
import com.facet.api.funding.model.FundRewards;
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
        private Long productIdx;
        private Long price;
        private List<OrdersItemReq> ordersItems;

        public FundOrders toEntity(User user, FundProduct product) {
            FundOrders orders = FundOrders.builder()
                    .ordersIdx(user.getIdx())
                    .status("PENDING")
                    .price(this.price)
                    .fundProduct(product)
                    .build();

            if (this.ordersItems != null) {
                this.ordersItems.forEach(item -> {
                    // 1. 해당 상품(product)이 가진 리워드 리스트에서 현재 아이템의 ID와 일치하는 것을 찾습니다.
                    FundRewards matchingReward = product.getFundRewardsList().stream()
                            .filter(r -> r.getIdx().equals(item.getProductIdx()))
                            .findFirst()
                            .orElse(null); // 또는 예외 처리

                    // 2. Builder에 .fundRewards(matchingReward)를 반드시 추가해야 합니다.
                    orders.getOrdersItems().add(FundOrdersItem.builder()
                            .productIdx(item.getProductIdx())
                            .quantity(item.getQuantity())
                            .fundOrders(orders)
                            .fundRewards(matchingReward) // [이 부분이 핵심!] 이 코드가 있어야 reward_idx가 채워집니다.
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