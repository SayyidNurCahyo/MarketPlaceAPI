package com.enigma.marketplace.MarketPlaceAPI.repository;

import com.enigma.marketplace.MarketPlaceAPI.entity.ClaimReward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimRewardRepository extends JpaRepository<ClaimReward, String> {
    @Query(value = "select * from t_claim_reward where customer_id = :id", nativeQuery = true)
    Page<ClaimReward> getClaimByCustomerId(@Param("id") String id, Pageable pageable);
}
