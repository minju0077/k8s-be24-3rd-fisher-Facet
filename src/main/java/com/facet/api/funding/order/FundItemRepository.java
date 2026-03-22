package com.facet.api.funding.order;

import com.facet.api.funding.order.model.FundOrdersItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundItemRepository extends JpaRepository<FundOrdersItem,Long> {
}
