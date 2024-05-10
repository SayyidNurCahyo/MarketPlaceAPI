package com.enigma.marketplace.MarketPlaceAPI.service;

import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.MerchantResponse;
import com.enigma.marketplace.MarketPlaceAPI.entity.Merchant;
import org.springframework.data.domain.Page;

public interface MerchantService {
    void addMerchant(Merchant merchant);
    MerchantResponse getMerchantById(String id);
    Page<MerchantResponse> getAllMerchant(SearchRequest request);
    void disableById(String id);
}
