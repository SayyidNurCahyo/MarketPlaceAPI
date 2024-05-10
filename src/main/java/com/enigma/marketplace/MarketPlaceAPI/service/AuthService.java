package com.enigma.marketplace.MarketPlaceAPI.service;

import com.enigma.marketplace.MarketPlaceAPI.dto.request.AuthRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.RegisterMerchantRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.RegisterRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.LoginResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerCustomer(RegisterRequest request);
    RegisterResponse registerMerchant(RegisterMerchantRequest request);
    LoginResponse login(AuthRequest request);
    boolean validateToken();
}
