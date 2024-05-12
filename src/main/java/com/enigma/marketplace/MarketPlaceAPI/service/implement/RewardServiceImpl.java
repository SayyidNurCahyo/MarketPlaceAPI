package com.enigma.marketplace.MarketPlaceAPI.service.implement;

import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewRewardRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.UpdateRewardRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.RewardResponse;
import com.enigma.marketplace.MarketPlaceAPI.entity.Reward;
import com.enigma.marketplace.MarketPlaceAPI.repository.RewardRepository;
import com.enigma.marketplace.MarketPlaceAPI.service.RewardService;
import com.enigma.marketplace.MarketPlaceAPI.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {
    private final RewardRepository rewardRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RewardResponse addReward(NewRewardRequest request) {
        Reward reward = Reward.builder().name(request.getName()).point(request.getPoint()).isActive(true).build();
        Reward rewardSaved = rewardRepository.saveAndFlush(reward);
        return convertToRewardResponse(rewardSaved);
    }

    @Transactional(readOnly = true)
    @Override
    public RewardResponse getRewardById(String id) {
        Reward reward = rewardRepository.findRewardById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reward Data Not Found"));
        return convertToRewardResponse(reward);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RewardResponse> getAllReward(SearchRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if (request.getName() != null) return convertToPageRewardResponse(rewardRepository.findRewardByName("%"+request.getName()+"%", page));
        else return convertToPageRewardResponse(rewardRepository.findAllReward(page));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RewardResponse updateReward(UpdateRewardRequest request) {
        Reward reward = rewardRepository.findRewardById(request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reward Data Not Found"));
        reward.setName(request.getName());
        reward.setPoint(request.getPoint());
        Reward rewardSaved = rewardRepository.saveAndFlush(reward);
        return convertToRewardResponse(rewardSaved);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disableRewardById(String id) {
        Reward reward = rewardRepository.findRewardById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reward Data Not Found"));
        reward.setIsActive(false);
        rewardRepository.saveAndFlush(reward);
    }

    private RewardResponse convertToRewardResponse(Reward reward){
        return RewardResponse.builder().id(reward.getId()).rewardName(reward.getName())
                .rewardPoint(reward.getPoint()).build();
    }

    private Page<RewardResponse> convertToPageRewardResponse(Page<Reward> rewards){
        return rewards.map(this::convertToRewardResponse);
    }
}
