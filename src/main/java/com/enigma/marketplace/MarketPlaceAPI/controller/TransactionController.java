package com.enigma.marketplace.MarketPlaceAPI.controller;

import com.enigma.marketplace.MarketPlaceAPI.constant.APIUrl;
import com.enigma.marketplace.MarketPlaceAPI.constant.ResponseMessage;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewTransactionRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchTransactionRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CommonResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.PagingResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.TransactionResponse;
import com.enigma.marketplace.MarketPlaceAPI.security.AuthenticatedUser;
import com.enigma.marketplace.MarketPlaceAPI.service.TransactionService;
import com.enigma.marketplace.MarketPlaceAPI.util.ValidationUtil;
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
@RequestMapping(path = APIUrl.TRANSACTION)
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final AuthenticatedUser authenticatedUser;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<TransactionResponse>> addTransaction(@RequestBody NewTransactionRequest request){
        TransactionResponse transactionResponse = transactionService.addTransaction(request);
        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.CREATED.value()).message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(transactionResponse).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<TransactionResponse>>> getAllTransaction(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "date") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction
    ){
        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .page(page).size(size).sortBy(sortBy).direction(direction).build();
        Page<TransactionResponse> transactions = transactionService.getAllTransaction(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(transactions.getTotalPages())
                .totalElement(transactions.getTotalElements())
                .page(transactions.getPageable().getPageNumber()+1)
                .size(transactions.getPageable().getPageSize())
                .hasNext(transactions.hasNext())
                .hasPrevious(transactions.hasPrevious()).build();
        CommonResponse<List<TransactionResponse>> response = CommonResponse.<List<TransactionResponse>>builder()
                .statusCode(HttpStatus.OK.value()).message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transactions.getContent()).paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasCustomerId(#id)")
    @GetMapping(path = "/customers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<TransactionResponse>>> getAllTransactionByCustomerId(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "date") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @PathVariable String id
    ){
        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .page(page).size(size).sortBy(sortBy).direction(direction).build();
        Page<TransactionResponse> transactions = transactionService.getTransactionByCustomerId(id, request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(transactions.getTotalPages())
                .totalElement(transactions.getTotalElements())
                .page(transactions.getPageable().getPageNumber()+1)
                .size(transactions.getPageable().getPageSize())
                .hasNext(transactions.hasNext())
                .hasPrevious(transactions.hasPrevious()).build();
        CommonResponse<List<TransactionResponse>> response = CommonResponse.<List<TransactionResponse>>builder()
                .statusCode(HttpStatus.OK.value()).message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transactions.getContent()).paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasMerchantId(#id)")
    @GetMapping(path = "/merchants/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<TransactionResponse>>> getAllTransactionByMerchantId(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "date") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @PathVariable String id
    ){
        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .page(page).size(size).sortBy(sortBy).direction(direction).build();
        Page<TransactionResponse> transactions = transactionService.getTransactionByMerchantId(id, request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(transactions.getTotalPages())
                .totalElement(transactions.getTotalElements())
                .page(transactions.getPageable().getPageNumber()+1)
                .size(transactions.getPageable().getPageSize())
                .hasNext(transactions.hasNext())
                .hasPrevious(transactions.hasPrevious()).build();
        CommonResponse<List<TransactionResponse>> response = CommonResponse.<List<TransactionResponse>>builder()
                .statusCode(HttpStatus.OK.value()).message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transactions.getContent()).paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }
}
