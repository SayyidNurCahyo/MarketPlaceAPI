package com.enigma.marketplace.MarketPlaceAPI.service;

import com.enigma.marketplace.MarketPlaceAPI.dto.response.JwtClaims;
import com.enigma.marketplace.MarketPlaceAPI.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount account);
    Boolean verifyJwtToken(String token);
    JwtClaims getClaimsByToken(String token);
}
