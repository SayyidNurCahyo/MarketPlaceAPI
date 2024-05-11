package com.enigma.marketplace.MarketPlaceAPI.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchTransactionRequest {
    private Integer size;
    private Integer page;
    private String sortBy;
    private String direction;
}
