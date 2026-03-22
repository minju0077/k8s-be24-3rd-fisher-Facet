package com.facet.api.funding;

import com.facet.api.funding.model.FundProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundingRepository extends JpaRepository<FundProduct,Long> {
    Page<FundProduct> findByCategory(String category, PageRequest sort);

    Page<FundProduct> findByDays(int endDay, PageRequest pageRequest);
}
