package com.enigma.marketplace.MarketPlaceAPI.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantResponse {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String username;
}
