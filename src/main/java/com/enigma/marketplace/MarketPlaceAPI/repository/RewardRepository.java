package com.enigma.marketplace.MarketPlaceAPI.repository;

import com.enigma.marketplace.MarketPlaceAPI.entity.Reward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardRepository extends JpaRepository<Reward, String> {
    @Query(value = "select * from m_reward where id = :id and is_active = true", nativeQuery = true)
    Optional<Reward> findRewardById(@Param("id") String id);
    @Query(value = "select * from m_reward where is_active = true", nativeQuery = true)
    Page<Reward> findAllReward(Pageable pageable);
    @Query(value = "select * from m_reward where name like :name and is_active = true", nativeQuery = true)
    Page<Reward> findRewardByName(@Param("name") String name, Pageable pageable);
}
