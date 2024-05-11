package com.enigma.marketplace.MarketPlaceAPI.repository;

import com.enigma.marketplace.MarketPlaceAPI.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String > {
    @Query(value = "select * from t_transaction where customer_id = :customerId", nativeQuery = true)
    Page<Transaction> findByCustomerId(@Param("customerId") String customerId, Pageable pageable);
    @Query(value = "select * from t_transaction where merchant_id = :merchantId", nativeQuery = true)
    Page<Transaction> findByMerchantId(@Param("merchantId") String merchantId, Pageable pageable);
}
