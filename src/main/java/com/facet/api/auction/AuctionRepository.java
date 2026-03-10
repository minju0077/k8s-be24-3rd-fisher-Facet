package com.facet.api.auction;

import com.facet.api.auction.model.AucProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<AucProduct, Long> {
    Page<AucProduct> findAllByStatusLessThanEqual(int attr0, Pageable pageable);
    // AuctionRepository.java
    Page<AucProduct> findAllByStatusLessThanEqualAndNameContaining(int status, String search, Pageable pageRequest);
}
