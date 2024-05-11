package com.enigma.marketplace.MarketPlaceAPI.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewRewardRequest {
    @NotBlank(message = "Reward Name is Required")
    private String name;
    @NotNull(message = "Reward Point is Required")
    @Min(value = 1, message = "Reward point Must be Greater Than 0")
    private Integer point;
}
