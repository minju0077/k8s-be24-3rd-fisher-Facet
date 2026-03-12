package com.facet.api.auction.repository;

import com.facet.api.auction.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionBidRepository extends JpaRepository<Bid, Long> {
}
