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
public class UpdateProductRequest {
    @NotBlank(message = "ID Product is Required")
    private String id;
    @NotBlank(message = "Product Name is Required")
    private String name;
    @NotNull(message = "Product Price is Required")
    @Min(value = 1, message = "Product Price must be Greater Than 0")
    private Long price;
    @NotNull(message = "Product Stock is Required")
    @Min(value = 0, message = "Product Stock must be Greater Than or Equal 0")
    private Integer stock;
}
