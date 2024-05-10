package com.enigma.marketplace.MarketPlaceAPI.controller;

import com.enigma.marketplace.MarketPlaceAPI.constant.APIUrl;
import com.enigma.marketplace.MarketPlaceAPI.constant.ResponseMessage;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.AuthRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.RegisterMerchantRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.RegisterRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CommonResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.LoginResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.RegisterResponse;
import com.enigma.marketplace.MarketPlaceAPI.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = APIUrl.AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<?>> registerCustomer(@RequestBody RegisterRequest request) {
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value()).message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(authService.registerCustomer(request)).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/registerMerchant", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<?>> registerMerchant(@RequestBody RegisterMerchantRequest request) {
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value()).message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(authService.registerMerchant(request)).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<?>> login(@RequestBody AuthRequest request) {
        LoginResponse loginResponse = authService.login(request);
        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value()).message("Successfully Login")
                .data(loginResponse).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            value = "/validate-token",
            produces = {"application/json"}
    )
    public ResponseEntity<?> validateToken() {
        boolean valid = this.authService.validateToken();
        CommonResponse response;
        if (valid) {
            response = CommonResponse.builder().statusCode(HttpStatus.OK.value()).message("Successfully Fetch Data").build();
            return ResponseEntity.ok(response);
        } else {
            response = CommonResponse.builder().statusCode(HttpStatus.UNAUTHORIZED.value()).message("Invalid JWT").build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
