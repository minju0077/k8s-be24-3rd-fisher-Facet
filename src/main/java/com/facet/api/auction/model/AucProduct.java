package com.facet.api.auction.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class AucProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 상품 번호
    private Long idx;

    // 카테고리
    @Column(nullable = false)
    private String category;

    // 상품 이름
    @Column(nullable = false)
    private String name;

    // 상품 설명
    @Column(nullable = false)
    private String description;

    // 브랜드 이름
    @Column(nullable = false)
    private String brandName;

    // 상품 이미지
    @Column(nullable = false)
    private String image;

    // 경매 시작가
    @Column(nullable = false)
    private int startPrice;

    // 경매 시작 시간
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startAt;

    // 경매 종료 시간
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endAt;

    // 입찰 단위
    @ColumnDefault("10000")
    @Column(nullable = false)
    private int bidIncrement;

    // 경매 현재 입찰가
    @Column(nullable = false)
    private Long currentPrice;

    // 입찰 수
    @Column(nullable = false)
    private int bidCount;

    // 원산지
    @Column(nullable = false)
    private String origin;

    // 소재
    @Column(nullable = false)
    private String material;

    // 사이즈
    private String size;

    // 배송 방법
    private String shippingMethod;

    // 배송비
    private int shippingPrice;

    // 배송 기간
    private String shippingDuration;
}
