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
public class TransactionDetailRequest {
    @NotBlank(message = "ID Product is Required")
    private String productId;
    @NotNull(message = "Quantity is Required")
    @Min(value = 1, message = "Quantity must be Greater Than 0")
    private Integer quantity;
}
