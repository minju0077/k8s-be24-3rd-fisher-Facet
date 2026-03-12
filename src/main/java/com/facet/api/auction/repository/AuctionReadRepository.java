package com.facet.api.auction.repository;

import com.facet.api.auction.model.AucProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionReadRepository extends JpaRepository<AucProduct, Long> {

    // AuctionRepository.java
    Page<AucProduct> findAllByName(String search, Pageable pageRequest);

}
