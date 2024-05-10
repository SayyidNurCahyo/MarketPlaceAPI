package com.enigma.marketplace.MarketPlaceAPI.service.implement;

import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.UpdateCustomerRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CustomerResponse;
import com.enigma.marketplace.MarketPlaceAPI.entity.Customer;
import com.enigma.marketplace.MarketPlaceAPI.entity.UserAccount;
import com.enigma.marketplace.MarketPlaceAPI.repository.CustomerRepository;
import com.enigma.marketplace.MarketPlaceAPI.service.CustomerService;
import com.enigma.marketplace.MarketPlaceAPI.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addCustomer(Customer customer) {
        customerRepository.saveAndFlush(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerResponse getCustomerById(String id) {
        Customer customer = customerRepository.findByIdCustomer(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer Not Found"));
        return convertToCustomerResponse(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CustomerResponse> getAllCustomer(SearchRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getName()!=null){
            Page<Customer> customers = customerRepository.findCustomer("%"+request.getName()+"%", page);
            return convertToPageCustomerResponse(customers);
        }else {
            return convertToPageCustomerResponse(customerRepository.findAllCustomer(page));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse updateCustomer(UpdateCustomerRequest request) {
        validationUtil.validate(request);
        Customer customer = customerRepository.findById(request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer Not Found"));
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        UserAccount account = customer.getUserAccount();
        account.setUsername(request.getUsername());
        customer.setUserAccount(account);
        customerRepository.saveAndFlush(customer);
        return convertToCustomerResponse(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disableById(String id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer Not Found"));
        UserAccount account = customer.getUserAccount();
        account.setIsEnable(false);
        customer.setUserAccount(account);
        customerRepository.saveAndFlush(customer);
    }

    private CustomerResponse convertToCustomerResponse(Customer customer){
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phone(customer.getPhone())
                .username(customer.getUserAccount().getUsername())
                .point(customer.getPoint())
                .build();
    }

    private Page<CustomerResponse> convertToPageCustomerResponse(Page<Customer> customers){
        return customers.map(this::convertToCustomerResponse);
    }
}
