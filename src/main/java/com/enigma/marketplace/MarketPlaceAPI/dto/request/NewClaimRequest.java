package com.enigma.marketplace.MarketPlaceAPI.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewClaimRequest {
    @NotBlank(message = "ID Customer is Required")
    private String customerId;
    @NotBlank(message = "Claim Date is Required")
    private String date;
    @NotBlank(message = "ID Reward is Required")
    private String rewardId;
}
