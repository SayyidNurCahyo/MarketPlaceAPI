package com.enigma.marketplace.MarketPlaceAPI.repository;

import com.enigma.marketplace.MarketPlaceAPI.entity.ClaimReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimRewardRepository extends JpaRepository<ClaimReward, String> {
}
