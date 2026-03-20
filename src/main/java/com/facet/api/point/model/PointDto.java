package com.facet.api.point.model;

import com.facet.api.user.model.User;
import lombok.Builder;
import lombok.Getter;

public class PointDto {

    //  [1 단계 요청] 프론트 -> 백엔드 포인트 고르고 충전 요청
    @Getter
    @Builder
    public static class CreateReq {
        private Integer amount; //  프론트에서 보내주는 충전 금액

        // DTO -> Entity로 변환 머서드
        public Point toEntity(User user){
            return Point.builder()
                    .user(user)
                    .amount(this.amount)
                    .paid(false)
                    .build();  //pgPaymentId, paidAt은 아직 결제 전이라 null
                                // createAt은 우리가 달아둔 @CreatedDate가 알아서 현재 시간으로 넣어줌
        }
    }

    //  [1단계 응답] 백엔드 -> 프론트
    @Getter
    @Builder
    public static class CreateRes {
        private Long pointIdx;  //  방금 DB에 저장된 point 테이블 PK 번호
        private int amount;     //  충전할 금액 확인용

        public static CreateRes from(Point entity) {
            return CreateRes.builder()
                    .pointIdx(entity.getIdx())
                    .amount(entity.getAmount())
                    .build();
        }
    }

    // [2단계 요청] 프론트 -> 백엔드 : 결제 요청
    @Getter
    @Builder
    public static class VerifyReq {
        private String paymentId;   // 포트원이 발급한 영수증 번호
        private Long pointIdx;      //  1단계 응답에서 프론트가 받아갔던 db PK 번

    }

    // 포인트 불러와 프론트에 표시
    @Getter
    @Builder
    public static class CurrentRes {
        private int currentPoint; // 현재 보유 포인트
    }
}

