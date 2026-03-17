package com.facet.api.funding;

import com.facet.api.funding.model.FundingProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundingRepository extends JpaRepository<FundingProduct,Long> {
    Page<FundingProduct> findByCategory(String category, PageRequest sort);

    Page<FundingProduct> findByDays(int endDay, PageRequest pageRequest);
}
