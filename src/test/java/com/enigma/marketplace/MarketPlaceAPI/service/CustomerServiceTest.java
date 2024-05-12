package com.enigma.marketplace.MarketPlaceAPI.service;

import com.enigma.marketplace.MarketPlaceAPI.constant.UserRole;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.SearchRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.UpdateCustomerRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.CustomerResponse;
import com.enigma.marketplace.MarketPlaceAPI.entity.Customer;
import com.enigma.marketplace.MarketPlaceAPI.entity.Role;
import com.enigma.marketplace.MarketPlaceAPI.entity.UserAccount;
import com.enigma.marketplace.MarketPlaceAPI.repository.CustomerRepository;
import com.enigma.marketplace.MarketPlaceAPI.service.implement.CustomerServiceImpl;
import com.enigma.marketplace.MarketPlaceAPI.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private CustomerService customerService;
    @BeforeEach
    void setUp(){
        customerService = new CustomerServiceImpl(customerRepository, validationUtil);
    }

    @Test
    void addCustomer() {
        Customer customer = Customer.builder().id("tes id").name("cahyo").phone("088888")
                .userAccount(UserAccount.builder().id("tes account id")
                        .password("passwordTes").username("cahyo123")
                        .isEnable(true).roles(List.of(Role.builder().id("tes role id")
                                .role(UserRole.ROLE_CUSTOMER).build())).build()).build();
        when(customerRepository.saveAndFlush(Mockito.any(Customer.class))).thenReturn(customer);
        Customer result = customerRepository.saveAndFlush(customer);
        assertEquals(result.getUserAccount().getUsername(), customer.getUserAccount().getUsername());
    }

    @Test
    void getCustomerById() {
        String id = "tes id";
        Customer customer = Customer.builder().id("tes id").name("cahyo").phone("088888").point(1)
                .userAccount(UserAccount.builder().id("tes account id")
                        .password("passwordTes").username("cahyo123")
                        .isEnable(true).roles(List.of(Role.builder().id("tes role id")
                                .role(UserRole.ROLE_CUSTOMER).build())).build()).build();
        Mockito.when(customerRepository.findByIdCustomer(id)).thenReturn(Optional.of(customer));
        CustomerResponse result = customerService.getCustomerById(id);
        assertEquals(result.getUsername(), customer.getUserAccount().getUsername());
    }

    @Test
    void getAllCustomer() {
        SearchRequest request = SearchRequest.builder().size(5)
                .page(1).direction("asc").sortBy("name").build();
        Page<Customer> customerPage = new PageImpl<>(Collections.emptyList());
        Mockito.when(customerRepository.findAllCustomer(Mockito.any(Pageable.class))).thenReturn(customerPage);
        Page<CustomerResponse> result = customerService.getAllCustomer(request);
        assertEquals(customerPage.getTotalElements(), result.getTotalElements());
        assertEquals(customerPage.getTotalPages(), result.getTotalPages());
        assertEquals(customerPage.getNumber(), result.getNumber());
        assertEquals(customerPage.getSize(), result.getSize());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void updateCustomer() {
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setId("tesId");
        request.setName("Cahyo");
        request.setPhone("1234567890");
        request.setUsername("cahyo123");
        Customer customer = new Customer();
        customer.setId("tesId");
        customer.setName("Cahyo2");
        customer.setPhone("0987654321");
        UserAccount account = UserAccount.builder().id("tes account id")
                .password("passwordTes").username("cahyo123")
                .isEnable(true).roles(List.of(Role.builder().id("tes role id")
                        .role(UserRole.ROLE_CUSTOMER).build())).build();
        account.setUsername("Cahyo234");
        customer.setUserAccount(account);
        when(customerRepository.findById(request.getId())).thenReturn(Optional.of(customer));
        CustomerResponse response = customerService.updateCustomer(request);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getPhone(), response.getPhone());
        assertEquals(request.getUsername(), response.getUsername());
    }

    @Test
    void deleteById() {
        Customer customer = new Customer();
        customer.setId("tesId");
        customer.setName("Cahyo");
        customer.setPhone("1234567890");
        UserAccount userAccount = UserAccount.builder().id("tes account id")
                .password("passwordTes").username("cahyo123")
                .isEnable(false).roles(List.of(Role.builder().id("tes role id")
                        .role(UserRole.ROLE_CUSTOMER).build())).build();
        customer.setUserAccount(userAccount);
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);
        Customer customer1 = customerRepository.saveAndFlush(customer);
        assertEquals(customer1.getUserAccount().getIsEnable(), false);
    }
}
