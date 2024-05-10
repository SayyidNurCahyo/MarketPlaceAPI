package com.enigma.marketplace.MarketPlaceAPI.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Username is Required")
    @Min(value = 8, message = "Username at Least 8 Character")
    private String username;
    @NotBlank(message = "Password is Required")
    @Min(value = 8, message = "Password at Least 8 Character")
    private String password;
    @NotBlank(message = "Name is Required")
    private String name;
    @NotBlank(message = "Phone Number is Required")
    private String phone;
}
