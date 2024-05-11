package com.enigma.marketplace.MarketPlaceAPI.service;

import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewTransactionRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchTransactionRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

public interface TransactionService {
    TransactionResponse addTransaction(NewTransactionRequest request);
    Page<TransactionResponse> getAllTransaction(SearchTransactionRequest request);
    Page<TransactionResponse> getTransactionByCustomerId(String id, SearchTransactionRequest request);
    Page<TransactionResponse> getTransactionByMerchantId(String id, SearchTransactionRequest request);
}
