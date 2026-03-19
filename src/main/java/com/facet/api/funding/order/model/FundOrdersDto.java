package com.facet.api.funding.order.model;

import com.facet.api.user.model.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class FundOrdersDto {

    @Getter
    public static class OrdersReq{
        private Long paymentPrice;
            private List<Long> fundIdxList;

        public FundOrders toEntity(User user){
            return FundOrders.builder()
                    .paid(false)
                    .paymentPrice(this.paymentPrice)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class OrdersRes{
        private Long ordersIdx;
        private Boolean paid;

        public static OrdersRes from(FundOrders entity){
            return OrdersRes.builder()
                    .ordersIdx(entity.getIdx())
                    .paid(entity.isPaid())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class VerifyReq {
        private String paymentId;
    }
}
