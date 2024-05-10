package com.enigma.marketplace.MarketPlaceAPI.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCustomerRequest {
    @NotBlank(message = "Id is Required")
    private String id;
    @NotBlank(message = "Name is Required")
    private String name;
    @NotBlank(message = "Phone Number is Required")
    private String phone;
    @NotBlank(message = "Username is Required")
    private String username;
}
