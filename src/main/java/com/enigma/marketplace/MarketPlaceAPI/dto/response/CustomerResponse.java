package com.enigma.marketplace.MarketPlaceAPI.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private String id;
    private String name;
    private String phone;
    private Integer point;
    private String username;
}
