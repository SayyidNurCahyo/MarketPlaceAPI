package com.enigma.marketplace.MarketPlaceAPI.service;

import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewRewardRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.UpdateRewardRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.RewardResponse;
import org.springframework.data.domain.Page;

public interface RewardService {
    RewardResponse addReward(NewRewardRequest request);
    RewardResponse getRewardById(String id);
    Page<RewardResponse> getAllReward(SearchRequest request);
    RewardResponse updateReward(UpdateRewardRequest request);
    void disableRewardById(String id);
}
