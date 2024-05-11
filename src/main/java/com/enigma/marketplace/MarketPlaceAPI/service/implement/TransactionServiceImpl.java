package com.enigma.marketplace.MarketPlaceAPI.service.implement;

import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewTransactionRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchTransactionRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.*;
import com.enigma.marketplace.MarketPlaceAPI.entity.*;
import com.enigma.marketplace.MarketPlaceAPI.repository.TransactionRepository;
import com.enigma.marketplace.MarketPlaceAPI.service.*;
import com.enigma.marketplace.MarketPlaceAPI.util.DateUtil;
import com.enigma.marketplace.MarketPlaceAPI.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ValidationUtil validationUtil;
    private final CustomerService customerService;
    private final UserService userService;
    private final MerchantService merchantService;
    private final ProductService productService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse addTransaction(NewTransactionRequest request) {
        validationUtil.validate(request);
        CustomerResponse customerResponse = customerService.getCustomerById(request.getCustomerId());
        MerchantResponse merchantResponse = merchantService.getMerchantById(request.getMerchantId());
        Merchant merchant = Merchant.builder().id(merchantResponse.getId())
                .name(merchantResponse.getName())
                .phone(merchantResponse.getPhone())
                .address(merchantResponse.getAddress())
                .userAccount((UserAccount) userService.loadUserByUsername(merchantResponse.getUsername()))
                .products(productService.getAllProduct(merchantResponse.getId())).build();
        Transaction transaction = Transaction.builder()
                .date(DateUtil.parseDate(request.getDate(), "yyyy-MM-dd"))
                .customer(Customer.builder().id(customerResponse.getId())
                        .name(customerResponse.getName())
                        .phone(customerResponse.getPhone())
                        .userAccount((UserAccount) userService.loadUserByUsername(customerResponse.getUsername())).build())
                .merchant(merchant)
                .build();
        List<TransactionDetail> transactionDetail = request.getDetailRequests().stream().map(detail -> {
            ProductResponse productResponse = productService.getProductById(detail.getProductId(), request.getMerchantId());
            return TransactionDetail.builder().transaction(transaction)
                    .product(Product.builder().id(productResponse.getId())
                            .name(productResponse.getName())
                            .price(productResponse.getPrice())
                            .stock(productResponse.getStock())
                            .merchant(merchant).isActive(true).build())
                    .quantity(detail.getQuantity()).build();
        }).toList();
        transaction.setTransactionDetails(transactionDetail);
        customerService.updatePoint(request.getCustomerId(), customerResponse.getPoint() + 1);
        Transaction trSaved = transactionRepository.saveAndFlush(transaction);
        return convertToTransactionResponse(trSaved);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TransactionResponse> getAllTransaction(SearchTransactionRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        return convertToPageTransactionResponse(transactionRepository.findAll(page));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TransactionResponse> getTransactionByCustomerId(String id, SearchTransactionRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        return convertToPageTransactionResponse(transactionRepository.findByCustomerId(id, page));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TransactionResponse> getTransactionByMerchantId(String id, SearchTransactionRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        return convertToPageTransactionResponse(transactionRepository.findByMerchantId(id, page));
    }

    private TransactionResponse convertToTransactionResponse(Transaction transaction){
        List<TransactionDetailResponse> detailResponses = transaction.getTransactionDetails().stream().map(detail ->
                TransactionDetailResponse.builder().id(detail.getId())
                        .productName(detail.getProduct().getName())
                        .productPrice(detail.getProduct().getPrice())
                        .quantity(detail.getQuantity()).build()
        ).toList();
        return TransactionResponse.builder().id(transaction.getId())
                .customerName(transaction.getCustomer().getName())
                .merchantName(transaction.getMerchant().getName())
                .rewardPoint(transaction.getCustomer().getPoint()+1)
                .date(transaction.getDate().toString())
                .detailResponses(detailResponses).build();
    }

    private Page<TransactionResponse> convertToPageTransactionResponse(Page<Transaction> transactions){
        return transactions.map(this::convertToTransactionResponse);
    }
}
