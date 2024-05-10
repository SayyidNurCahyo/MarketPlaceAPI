package com.enigma.marketplace.MarketPlaceAPI.repository;

import com.enigma.marketplace.MarketPlaceAPI.entity.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {
    @Query(value = "select m.* from m_merchant as m join m_user_account as ua on ua.id = m.user_account_id where m.name like :name and ua.is_enable = true", nativeQuery = true)
    Page<Merchant> findMerchant(@Param("name") String name, Pageable pageable);

    @Query(value = "select m.* from m_merchant as m join m_user_account as ua on ua.id = m.user_account_id where ua.is_enable = true", nativeQuery = true)
    Page<Merchant> findAllMerchant(Pageable pageable);

    @Query(value = "select m.* from m_merchant as m join m_user_account as ua on ua.id = m.user_account_id where m.id = :id and ua.is_enable = true", nativeQuery = true)
    Optional<Merchant> findByIdMerchant(@Param("id") String id);
}
