package com.enigma.marketplace.MarketPlaceAPI.repository;

import com.enigma.marketplace.MarketPlaceAPI.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,String > {
    @Query(value = "select * from m_product where merchant_id = :id and is_active = true and name like :name", nativeQuery = true)
    Page<Product> findProduct(@Param("id") String merchantId, @Param("name") String name, Pageable pageable);
    @Query(value = "select * from m_product where merchant_id = :id and is_active = true", nativeQuery = true)
    Page<Product> findAllProduct(@Param("id") String merchantId, Pageable pageable);
    @Query(value = "select * from m_product where is_active = true and name like :name", nativeQuery = true)
    Page<Product> findProduct(@Param("name") String name, Pageable pageable);
    @Query(value = "select * from m_product where is_active = true", nativeQuery = true)
    Page<Product> findAllProduct(Pageable pageable);
    @Query(value = "select * from m_product where id = :id and is_active = true", nativeQuery = true)
    Optional<Product> findProductById(@Param("id") String id);
    @Query(value = "select * from m_product where id = :id and merchant_id = :merchantId and is_active = true", nativeQuery = true)
    Optional<Product> findProductById(@Param("id") String id, @Param("merchantId") String merchantId);
    @Query(value = "select * from m_product where merchant_id = :merchantId and is_active = true", nativeQuery = true)
    List<Product> findAllProduct(@Param("merchantId") String merchantId);
}
