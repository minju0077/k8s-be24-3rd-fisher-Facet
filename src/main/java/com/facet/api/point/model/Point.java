package com.facet.api.point.model;

import com.facet.api.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)  // 엔티티에 시간 데이터가 들어오는지 확인
@Builder
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;                  // 연결된 유저 객체 담아 두기

    @Column(nullable = false)
    private int amount;         //  유저가 충전하려고 누른 포인트 저장

    @Setter                     //  처음에는 false 였다가 결제를 하면 true로 바꿔야함
    @ColumnDefault("false")     // 기본 값을 false로 설정
    @Column(nullable = false)
    private boolean paid;       //  결제 완료 여부

    @Setter                     // 주문 준비엔 null 검증 후 포트원 고유 번호 업데이트
    private String pgPaymentId; // 돈이 결제되었음을 증명하는 포트원 영수증 번호

    @CreatedDate                // 이 데이터가 DB에 INSERT 될 때 시간 저장
    @Column(nullable = false,updatable = false)      // 생성 시 수정 불가
    private LocalDateTime createAt;     //  주문 생성 시간

    @Setter
    private LocalDateTime paidAt;   //  포트원 결제가 완벽하게 승인된 시간
}
