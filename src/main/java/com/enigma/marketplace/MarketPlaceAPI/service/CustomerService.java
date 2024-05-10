package com.enigma.marketplace.MarketPlaceAPI.service;

import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.UpdateCustomerRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CustomerResponse;
import com.enigma.marketplace.MarketPlaceAPI.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    void addCustomer(Customer customer);
    CustomerResponse getCustomerById(String id);
    Page<CustomerResponse> getAllCustomer(SearchRequest request);
    CustomerResponse updateCustomer(UpdateCustomerRequest customer);
    void disableById(String id);
}
