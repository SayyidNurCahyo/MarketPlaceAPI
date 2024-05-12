package com.enigma.marketplace.MarketPlaceAPI.controller;

import com.enigma.marketplace.MarketPlaceAPI.constant.APIUrl;
import com.enigma.marketplace.MarketPlaceAPI.constant.ResponseMessage;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.AuthRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewProductRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.RegisterMerchantRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.RegisterRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CommonResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.LoginResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.RegisterResponse;
import com.enigma.marketplace.MarketPlaceAPI.service.AuthService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @MockBean
    private AuthService authService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "ADMIN")
    @Test
    void registerCustomer() throws Exception{
        RegisterRequest request = RegisterRequest.builder().name("name").phone("0888").password("password").username("user").build();
        RegisterResponse registerResponse = RegisterResponse.builder().username("usern").roles(List.of("CUSTOMER")).build();
        when(authService.registerCustomer(any(RegisterRequest.class))).thenReturn(registerResponse);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.post(APIUrl.AUTH+"/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(stringRequest))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<RegisterResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<RegisterResponse>>() {});
                    assertEquals(201, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_SAVE_DATA, response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void registerAdmin() throws Exception{
        RegisterMerchantRequest request = RegisterMerchantRequest.builder()
                .username("user")
                .password("password")
                .name("name")
                .phone("0888")
                .address("address")
                .products(List.of(NewProductRequest.builder().name("productName")
                        .price(1L).stock(1).build()))
                .build();
        RegisterResponse registerResponse = RegisterResponse.builder().username("user").roles(List.of("MERCHANT")).build();
        when(authService.registerMerchant(any(RegisterMerchantRequest.class))).thenReturn(registerResponse);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.post(APIUrl.AUTH+"/registerMerchant")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(stringRequest))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<RegisterResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<RegisterResponse>>() {});
                    assertEquals(201, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_SAVE_DATA, response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void login() throws Exception{
        AuthRequest request = AuthRequest.builder().username("user").password("password").build();
        LoginResponse loginResponse = LoginResponse.builder().roles(List.of("ADMIN")).username("user").token("token").build();
        when(authService.login(any(AuthRequest.class))).thenReturn(loginResponse);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.post(APIUrl.AUTH+"/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(stringRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<LoginResponse>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Successfully Login", response.getMessage());
                } );
    }
}
