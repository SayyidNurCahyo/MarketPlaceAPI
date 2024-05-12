package com.enigma.marketplace.MarketPlaceAPI.service.implement;

import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewClaimRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchTransactionRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.ClaimRewardResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CustomerResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.RewardResponse;
import com.enigma.marketplace.MarketPlaceAPI.entity.ClaimReward;
import com.enigma.marketplace.MarketPlaceAPI.entity.Customer;
import com.enigma.marketplace.MarketPlaceAPI.entity.Reward;
import com.enigma.marketplace.MarketPlaceAPI.entity.UserAccount;
import com.enigma.marketplace.MarketPlaceAPI.repository.ClaimRewardRepository;
import com.enigma.marketplace.MarketPlaceAPI.service.ClaimRewardService;
import com.enigma.marketplace.MarketPlaceAPI.service.CustomerService;
import com.enigma.marketplace.MarketPlaceAPI.service.RewardService;
import com.enigma.marketplace.MarketPlaceAPI.service.UserService;
import com.enigma.marketplace.MarketPlaceAPI.util.DateUtil;
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
public class ClaimRewardServiceImpl implements ClaimRewardService {
    private final ClaimRewardRepository claimRewardRepository;
    private final ValidationUtil validationUtil;
    private final CustomerService customerService;
    private final UserService userService;
    private final RewardService rewardService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ClaimRewardResponse addClaimReward(NewClaimRequest request) {
        validationUtil.validate(request);
        CustomerResponse customerResponse = customerService.getCustomerById(request.getCustomerId());
        Customer customer = Customer.builder().id(customerResponse.getId())
                .name(customerResponse.getName())
                .phone(customerResponse.getPhone())
                .point(customerResponse.getPoint())
                .userAccount((UserAccount) userService.loadUserByUsername(customerResponse.getUsername())).build();
        RewardResponse rewardResponse = rewardService.getRewardById(request.getRewardId());
        Reward reward = Reward.builder().id(rewardResponse.getId())
                .name(rewardResponse.getRewardName())
                .point(rewardResponse.getRewardPoint())
                .isActive(true).build();
        if (customer.getPoint()< reward.getPoint()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient Customer Reward Point");
        ClaimReward claimReward = ClaimReward.builder().customer(customer)
                .date(DateUtil.parseDate(request.getDate(), "yyyy-MM-dd"))
                .reward(reward).build();
        ClaimReward claimRewardSaved = claimRewardRepository.saveAndFlush(claimReward);
        customerService.updatePoint(request.getCustomerId(), customer.getPoint() - reward.getPoint());
        Customer customer1 = claimRewardSaved.getCustomer();
        customer1.setPoint(customer1.getPoint()- reward.getPoint());
        claimRewardSaved.setCustomer(customer1);
        return convertToClaimResponse(claimRewardSaved);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ClaimRewardResponse> getAllClaim(SearchTransactionRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        return convertToPageClaimResponse(claimRewardRepository.findAll(page));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ClaimRewardResponse> getClaimByCustomerId(String customerId, SearchTransactionRequest request) {
        customerService.getCustomerById(customerId);
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        return convertToPageClaimResponse(claimRewardRepository.getClaimByCustomerId(customerId, page));
    }

    private ClaimRewardResponse convertToClaimResponse(ClaimReward claimReward){
        return ClaimRewardResponse.builder().id(claimReward.getId())
                .customerName(claimReward.getCustomer().getName())
                .customerPoint(claimReward.getCustomer().getPoint())
                .date(claimReward.getDate().toString())
                .rewardName(claimReward.getReward().getName()).build();
    }

    private Page<ClaimRewardResponse> convertToPageClaimResponse(Page<ClaimReward> claimRewards){
        return claimRewards.map(this::convertToClaimResponse);
    }
}
