package com.facet.api.auction;

import com.facet.api.auction.model.AucProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<AucProduct, Long> {
    List<AucProduct> findAllByStatusLessThanEqual(int attr0);
}
