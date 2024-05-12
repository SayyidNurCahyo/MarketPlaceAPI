package com.enigma.marketplace.MarketPlaceAPI.controller;

import com.enigma.marketplace.MarketPlaceAPI.constant.APIUrl;
import com.enigma.marketplace.MarketPlaceAPI.constant.ResponseMessage;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewClaimRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchTransactionRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.ClaimRewardResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CommonResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.PagingResponse;
import com.enigma.marketplace.MarketPlaceAPI.security.AuthenticatedUser;
import com.enigma.marketplace.MarketPlaceAPI.service.ClaimRewardService;
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
@RequestMapping(path = APIUrl.CLAIM_REWARD)
@RequiredArgsConstructor
public class ClaimRewardController {
    private final ClaimRewardService claimRewardService;
    private final AuthenticatedUser authenticatedUser;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<ClaimRewardResponse>> addClaim(@RequestBody NewClaimRequest request){
        ClaimRewardResponse claimRewardResponse = claimRewardService.addClaimReward(request);
        CommonResponse<ClaimRewardResponse> response = CommonResponse.<ClaimRewardResponse>builder()
                .statusCode(HttpStatus.CREATED.value()).message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(claimRewardResponse).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<ClaimRewardResponse>>> getAllClaim(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction
    ){
        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .page(page).size(size).sortBy(sortBy).direction(direction).build();
        Page<ClaimRewardResponse> claims = claimRewardService.getAllClaim(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(claims.getTotalPages())
                .totalElement(claims.getTotalElements())
                .page(claims.getPageable().getPageNumber()+1)
                .size(claims.getPageable().getPageSize())
                .hasNext(claims.hasNext())
                .hasPrevious(claims.hasPrevious()).build();
        CommonResponse<List<ClaimRewardResponse>> response = CommonResponse.<List<ClaimRewardResponse>>builder()
                .statusCode(HttpStatus.OK.value()).message(ResponseMessage.SUCCESS_GET_DATA)
                .data(claims.getContent()).paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasCustomerId(#id)")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<ClaimRewardResponse>>> getAllClaim(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @PathVariable String id
    ){
        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .page(page).size(size).sortBy(sortBy).direction(direction).build();
        Page<ClaimRewardResponse> claims = claimRewardService.getClaimByCustomerId(id, request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(claims.getTotalPages())
                .totalElement(claims.getTotalElements())
                .page(claims.getPageable().getPageNumber()+1)
                .size(claims.getPageable().getPageSize())
                .hasNext(claims.hasNext())
                .hasPrevious(claims.hasPrevious()).build();
        CommonResponse<List<ClaimRewardResponse>> response = CommonResponse.<List<ClaimRewardResponse>>builder()
                .statusCode(HttpStatus.OK.value()).message(ResponseMessage.SUCCESS_GET_DATA)
                .data(claims.getContent()).paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }
}
