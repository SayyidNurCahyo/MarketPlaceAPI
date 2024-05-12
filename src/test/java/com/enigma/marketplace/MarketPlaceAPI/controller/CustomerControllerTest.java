package com.enigma.marketplace.MarketPlaceAPI.controller;

import com.enigma.marketplace.MarketPlaceAPI.constant.APIUrl;
import com.enigma.marketplace.MarketPlaceAPI.constant.ResponseMessage;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.UpdateCustomerRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CommonResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CustomerResponse;
import com.enigma.marketplace.MarketPlaceAPI.service.CustomerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {
    @MockBean
    private CustomerService customerService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "ADMIN")
    @Test
    void getCustomerById() throws Exception{
        String id = UUID.randomUUID().toString();
        CustomerResponse customerResponse = CustomerResponse.builder().id(id).name("Cahyo").username("username").point(1).phone("088888").build();
        when(customerService.getCustomerById(anyString())).thenReturn(customerResponse);
        mockMvc.perform(MockMvcRequestBuilders.get(APIUrl.CUSTOMER+"/"+id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<CustomerResponse>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void getAllCustomer() throws Exception{
        SearchRequest request = SearchRequest.builder().page(1).size(10).sortBy("name").direction("asc").build();
        List<CustomerResponse> customerResponses = List.of(CustomerResponse.builder().id("id").name("Cahyo").username("username").point(1).phone("088888").build());
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        Page<CustomerResponse> customerResponsePage = new PageImpl<>(customerResponses,page, customerResponses.size());
        when(customerService.getAllCustomer(any(SearchRequest.class))).thenReturn(customerResponsePage);
        String stringRequest =objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.get(APIUrl.CUSTOMER).content(stringRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<List<CustomerResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<List<CustomerResponse>>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void updateCustomer() throws Exception{
        UpdateCustomerRequest request = UpdateCustomerRequest.builder().id("id").name("name").phone("08888").username("user").build();
        CustomerResponse customerResponse = CustomerResponse.builder().id(request.getId()).name(request.getName()).username(request.getUsername()).point(1).phone(request.getPhone()).build();
        when(customerService.updateCustomer(any(UpdateCustomerRequest.class))).thenReturn(customerResponse);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.put(APIUrl.CUSTOMER).contentType(MediaType.APPLICATION_JSON_VALUE).content(stringRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<CustomerResponse>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_UPDATE_DATA, response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void deleteById() throws Exception{
        String id = "id";
        CustomerResponse customerResponse = CustomerResponse.builder().id(id).build();
        when(customerService.updateCustomer(any(UpdateCustomerRequest.class))).thenReturn(customerResponse);
        mockMvc.perform(MockMvcRequestBuilders.delete(APIUrl.CUSTOMER+"/"+id).contentType(MediaType.APPLICATION_JSON_VALUE).content(id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<CustomerResponse>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_DELETE_DATA, response.getMessage());
                } );
    }
}
