package com.enigma.marketplace.MarketPlaceAPI.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    @NotBlank(message = "Username is Required")
    @Min(value = 8, message = "Username at Least 8 Character")
    private String username;
    @NotBlank(message = "Password is Required")
    @Min(value = 8, message = "Password at Least 8 Character")
    private String password;
}

