package com.enigma.marketplace.MarketPlaceAPI.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClaimRewardResponse {
    private String id;
    private String customerName;
    private Integer customerPoint;
    private String rewardName;
    private String date;
}
