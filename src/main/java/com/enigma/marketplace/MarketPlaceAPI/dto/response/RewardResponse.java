package com.enigma.marketplace.MarketPlaceAPI.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RewardResponse {
    private String id;
    private String rewardName;
    private Integer rewardPoint;
}
