package com.enigma.marketplace.MarketPlaceAPI.service;

import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewClaimRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchTransactionRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.ClaimRewardResponse;
import org.springframework.data.domain.Page;

public interface ClaimRewardService {
    ClaimRewardResponse addClaimReward(NewClaimRequest request);
    Page<ClaimRewardResponse> getAllClaim(SearchTransactionRequest request);
    Page<ClaimRewardResponse> getClaimByCustomerId(String customerId, SearchTransactionRequest request);
}
