package com.enigma.marketplace.MarketPlaceAPI.repository;

import com.enigma.marketplace.MarketPlaceAPI.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
