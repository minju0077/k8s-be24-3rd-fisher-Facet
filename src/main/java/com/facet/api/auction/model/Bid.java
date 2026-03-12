package com.facet.api.auction.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 입찰 id
    private Long idx;

    // 사용자 번호
    private Long userIdx;

    // 경매 상품 번호
    private Long aucProductIdx;

    // 입찰가
    private Long bidPrice;

    // 입찰 시간 // 입력 당시 시간 자동 저장
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;
}
