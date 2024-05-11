package com.enigma.marketplace.MarketPlaceAPI.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String id;
    private String customerName;
    private Integer rewardPoint;
    private String merchantName;
    private String date;
    private List<TransactionDetailResponse> detailResponses;
}
