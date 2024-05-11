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
public class UpdateProductReponse {
    @NotBlank(message = "ID Product is Required")
    private String id;
    @NotBlank(message = "Product Name is Required")
    private String name;
    @NotNull(message = "Product Price is Required")
    @Min(value = 1, message = "Product Price must be Greater Than 0")
    private Long price;
}
