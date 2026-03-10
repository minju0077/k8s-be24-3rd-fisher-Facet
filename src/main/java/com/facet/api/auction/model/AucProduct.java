package com.facet.api.auction.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class AucProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brandName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private int startPrice;

    @Column(nullable = false)
    private Date startAt;

    @Column(nullable = false)
    private Date endAt;

    @Column(nullable = false)
    private int status;

    @ColumnDefault("10000")
    @Column(nullable = false)
    private int bidIncrement;

    @Column(nullable = false)
    private Long currentPrice;

    public static AucDto.ListRes from(AucProduct entity) {
        return AucDto.ListRes.builder()
                .idx(entity.idx)
                .name(entity.name)
                .category(entity.category)
                .brandName(entity.brandName)
                .status(entity.status)
                .image(entity.image)
                .startPrice(entity.startPrice)
                .startAt(entity.startAt)
                .build();
    }
}
