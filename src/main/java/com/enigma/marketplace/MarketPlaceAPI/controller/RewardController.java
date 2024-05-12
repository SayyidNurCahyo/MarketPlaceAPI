package com.enigma.marketplace.MarketPlaceAPI.controller;

import com.enigma.marketplace.MarketPlaceAPI.constant.APIUrl;
import com.enigma.marketplace.MarketPlaceAPI.constant.ResponseMessage;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewRewardRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.UpdateCustomerRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.UpdateRewardRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CommonResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CustomerResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.PagingResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.RewardResponse;
import com.enigma.marketplace.MarketPlaceAPI.service.RewardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.REWARD)
@RequiredArgsConstructor
public class RewardController {
    private final RewardService rewardService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<RewardResponse>> addReward(@RequestBody NewRewardRequest request){
        RewardResponse rewardResponse = rewardService.addReward(request);
        CommonResponse<RewardResponse> response = CommonResponse.<RewardResponse>builder().statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA).data(rewardResponse).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<RewardResponse>> getRewardById(@PathVariable String id){
        RewardResponse rewardResponse = rewardService.getRewardById(id);
        CommonResponse<RewardResponse> response = CommonResponse.<RewardResponse>builder().statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA).data(rewardResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<RewardResponse>>> getAllReward(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name
    ) {
        SearchRequest request = SearchRequest.builder()
                .page(page).size(size).sortBy(sortBy).direction(direction)
                .name(name).build();
        Page<RewardResponse> rewards = rewardService.getAllReward(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(rewards.getTotalPages())
                .totalElement(rewards.getTotalElements())
                .page(rewards.getPageable().getPageNumber()+1)
                .size(rewards.getPageable().getPageSize())
                .hasNext(rewards.hasNext())
                .hasPrevious(rewards.hasPrevious()).build();
        CommonResponse<List<RewardResponse>> response = CommonResponse.<List<RewardResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(rewards.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<RewardResponse>> updateReward(@RequestBody UpdateRewardRequest request) {
        RewardResponse reward = rewardService.updateReward(request);
        CommonResponse<RewardResponse> response = CommonResponse.<RewardResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(reward).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> disableRewardById(@PathVariable String id) {
        rewardService.disableRewardById(id);
        CommonResponse response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        return ResponseEntity.ok(response);
    }
}
