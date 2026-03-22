package com.facet.api.funding.order;

import com.facet.api.funding.order.model.FundOrders;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface FundOrdersRepository extends JpaRepository<FundOrders,Long> {
    // 특정 상품(productIdx)에 대해 결제 완료(PAID)된 총 금액 합계
    @Query("SELECT SUM(o.price) FROM FundOrders o WHERE o.fundProduct.idx = :productIdx AND o.status = 'PAID'")
    Long sumPaidPriceByProductIdx(@Param("productIdx") Long productIdx);

    // 특정 상품(productIdx)에 대해 결제 완료(PAID)된 총 서포터 수 (주문 건수)
    @Query("SELECT COUNT(o.idx) FROM FundOrders o WHERE o.fundProduct.idx = :productIdx AND o.status = 'PAID'")
    Long countPaidSupportersByProductIdx(@Param("productIdx") Long productIdx);
}
