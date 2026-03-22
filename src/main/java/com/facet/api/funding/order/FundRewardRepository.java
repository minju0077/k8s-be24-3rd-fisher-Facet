package com.facet.api.funding.order;

import com.facet.api.funding.model.FundRewards;
import com.facet.api.funding.order.model.FundOrdersDto;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FundRewardRepository extends JpaRepository<FundRewards,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
    @Query("select r from FundRewards r where r.idx in :ids")
    List<FundRewards> findAllByIdWithPessimisticLock(@Param("ids") List<Long> ids);
}
