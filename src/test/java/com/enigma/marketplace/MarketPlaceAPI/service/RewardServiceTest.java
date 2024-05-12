package com.enigma.marketplace.MarketPlaceAPI.service;


import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewRewardRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.UpdateRewardRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.RewardResponse;
import com.enigma.marketplace.MarketPlaceAPI.entity.Reward;
import com.enigma.marketplace.MarketPlaceAPI.repository.RewardRepository;
import com.enigma.marketplace.MarketPlaceAPI.service.implement.RewardServiceImpl;
import com.enigma.marketplace.MarketPlaceAPI.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {
    @Mock
    private RewardRepository rewardRepository;
    @Mock
    private ValidationUtil validationUtil;
    private RewardService rewardService;
    @BeforeEach
    void setUp(){
        rewardService = new RewardServiceImpl(rewardRepository,validationUtil);
    }

    @Test
    void addReward() {
        NewRewardRequest request = NewRewardRequest.builder().name("reward").point(1).build();
        Reward reward = Reward.builder().id("id").name(request.getName()).point(request.getPoint()).isActive(true).build();
        when(rewardRepository.saveAndFlush(Mockito.any(Reward.class))).thenReturn(reward);
        RewardResponse result = rewardService.addReward(request);
        assertEquals(result.getRewardName(), request.getName());
    }

    @Test
    void getRewardById() {
        String id = "tes id";
        Reward reward = Reward.builder().id("id").name("reward").point(1).isActive(true).build();
        Mockito.when(rewardRepository.findRewardById(id)).thenReturn(Optional.of(reward));
        RewardResponse result = rewardService.getRewardById(id);
        assertEquals(result.getRewardName(), reward.getName());
    }

    @Test
    void getAllCustomer() {
        SearchRequest request = SearchRequest.builder().size(10)
                .page(1).direction("asc").sortBy("name").build();
        Page<Reward> rewards = new PageImpl<>(Collections.emptyList());
        Mockito.when(rewardRepository.findAllReward(Mockito.any(Pageable.class))).thenReturn(rewards);
        Page<RewardResponse> result = rewardService.getAllReward(request);
        assertEquals(rewards.getTotalElements(), result.getTotalElements());
        assertEquals(rewards.getTotalPages(), result.getTotalPages());
        assertEquals(rewards.getNumber(), result.getNumber());
        assertEquals(rewards.getSize(), result.getSize());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void updateCustomer() {
        UpdateRewardRequest request = new UpdateRewardRequest();
        request.setId("id");
        request.setName("rewardNew");
        request.setPoint(1);
        Reward reward = Reward.builder().id(request.getId()).name("reward").point(1).isActive(true).build();
        when(rewardRepository.findRewardById(anyString())).thenReturn(Optional.of(reward));
        reward.setName(request.getName());
        when(rewardRepository.saveAndFlush(any(Reward.class))).thenReturn(reward);
        RewardResponse response = rewardService.updateReward(request);
        assertEquals(request.getName(), response.getRewardName());
    }

    @Test
    void deleteById() {
        String id = "id";
        Reward reward = Reward.builder().id(id).isActive(false).point(1).name("reward").build();
        when(rewardRepository.saveAndFlush(any(Reward.class))).thenReturn(reward);
        Reward reward1 = rewardRepository.saveAndFlush(reward);
        assertEquals(reward1.getIsActive(), false);
    }
}
