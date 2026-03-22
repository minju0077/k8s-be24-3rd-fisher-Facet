package com.facet.api.funding.order;

import com.facet.api.funding.model.FundRewards;
import com.facet.api.funding.order.model.FundOrdersDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FundRewardRepository extends JpaRepository<FundRewards,Long> {
}
