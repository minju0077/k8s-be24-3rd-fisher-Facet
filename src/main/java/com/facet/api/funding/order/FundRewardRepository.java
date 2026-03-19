package com.facet.api.funding.order;

import com.facet.api.funding.model.FundRewards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundRewardRepository extends JpaRepository<FundRewards,Long> {
}
