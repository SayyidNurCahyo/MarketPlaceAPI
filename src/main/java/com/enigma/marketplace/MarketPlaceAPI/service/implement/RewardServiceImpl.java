package com.enigma.marketplace.MarketPlaceAPI.service.implement;

import com.enigma.marketplace.MarketPlaceAPI.dto.request.RewardRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.UpdateRewardRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.RewardResponse;
import com.enigma.marketplace.MarketPlaceAPI.entity.Reward;
import com.enigma.marketplace.MarketPlaceAPI.repository.RewardRepository;
import com.enigma.marketplace.MarketPlaceAPI.service.RewardService;
import com.enigma.marketplace.MarketPlaceAPI.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {
    private final RewardRepository rewardRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RewardResponse addReward(RewardRequest request) {
        Reward reward = Reward.builder().name(request.getName()).point(request.getPoint()).build();
        Reward rewardSaved = rewardRepository.saveAndFlush(reward);
        return convertToRewardResponse(rewardSaved);
    }

    @Transactional(readOnly = true)
    @Override
    public RewardResponse getRewardById(String id) {
        Reward reward = rewardRepository
    }

    @Override
    public Page<RewardResponse> getAllReward(SearchRequest request) {
        return null;
    }

    @Override
    public RewardResponse updateReward(UpdateRewardRequest request) {
        return null;
    }

    @Override
    public void disableRewardById(String id) {

    }

    private RewardResponse convertToRewardResponse(Reward reward){
        return RewardResponse.builder().id(reward.getId()).rewardName(reward.getName())
                .rewardPoint(reward.getPoint()).build();
    }

    private Page<RewardResponse> convertToPageRewardResponse(Page<Reward> rewards){
        return rewards.map(this::convertToRewardResponse);
    }
}
