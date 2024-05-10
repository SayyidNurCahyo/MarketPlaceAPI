package com.enigma.marketplace.MarketPlaceAPI.service;

import com.enigma.marketplace.MarketPlaceAPI.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserAccount getByUserId(String id);
    UserAccount getByContext();
}
