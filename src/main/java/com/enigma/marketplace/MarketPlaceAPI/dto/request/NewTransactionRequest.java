package com.enigma.marketplace.MarketPlaceAPI.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewTransactionRequest {
    @NotBlank(message = "ID Customer is Required")
    private String customerId;
    @NotBlank(message = "ID Merchant is Required")
    private String merchantId;
    @NotBlank(message = "Transaction Date is Required")
    private String date;
    @NotNull(message = "Transaction Detail is Required")
    private List<TransactionDetailRequest> detailRequests;
}
