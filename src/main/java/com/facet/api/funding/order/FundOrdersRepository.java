package com.facet.api.funding.order;

import com.facet.api.funding.order.model.FundOrders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundOrdersRepository extends JpaRepository<FundOrders,Long> {
}
