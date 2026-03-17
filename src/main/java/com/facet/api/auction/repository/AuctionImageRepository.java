package com.facet.api.auction.repository;

import com.facet.api.auction.model.AucProdImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionImageRepository extends JpaRepository<AucProdImage, Long> {
    public AucProdImage findByAucProductIdx(Long aucProductIdx);
}
