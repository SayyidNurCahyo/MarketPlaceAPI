package com.enigma.marketplace.MarketPlaceAPI.repository;

import com.enigma.marketplace.MarketPlaceAPI.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepository extends JpaRepository<Reward, String> {
}
