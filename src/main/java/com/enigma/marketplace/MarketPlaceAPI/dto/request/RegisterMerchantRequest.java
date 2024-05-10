package com.enigma.marketplace.MarketPlaceAPI.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterMerchantRequest {
    @NotBlank(message = "Merchant Name is Required")
    private String name;
    @NotNull(message = "Merchant Product is Required")
    private List<NewProductRequest> products;
    @NotBlank(message = "Phone Number is Required")
    private String phone;
    @NotBlank(message = "Address is Required")
    private String address;
    @NotBlank(message = "Username is Required")
    @Min(value = 8, message = "Username at Least 8 Character")
    private String username;
    @NotBlank(message = "Password is Required")
    @Min(value = 8, message = "Password at Least 8 Character")
    private String password;
}
