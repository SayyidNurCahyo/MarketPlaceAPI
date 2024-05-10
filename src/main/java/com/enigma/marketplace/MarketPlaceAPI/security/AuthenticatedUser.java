package com.enigma.marketplace.MarketPlaceAPI.security;

import com.enigma.marketplace.MarketPlaceAPI.dto.response.CustomerResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.MerchantResponse;
import com.enigma.marketplace.MarketPlaceAPI.entity.Customer;
import com.enigma.marketplace.MarketPlaceAPI.entity.UserAccount;
import com.enigma.marketplace.MarketPlaceAPI.service.CustomerService;
import com.enigma.marketplace.MarketPlaceAPI.service.MerchantService;
import com.enigma.marketplace.MarketPlaceAPI.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class AuthenticatedUser {
    private final CustomerService customerService;
    private final MerchantService merchantService;
    private final UserService userService;
    public boolean hasCustomerId(String customerId){
        CustomerResponse customerResponse = customerService.getCustomerById(customerId);
        UserAccount customerAccount = (UserAccount) userService.loadUserByUsername(customerResponse.getUsername());
        UserAccount userAccount = userService.getByContext();
        if (!userAccount.getId().equals(customerAccount.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden, Access Denied");
        }
        return true;
    }

    public boolean hasMerchantId(String merchantId){
        MerchantResponse merchantResponse = merchantService.getMerchantById(merchantId);
        UserAccount merchantAccount = (UserAccount) userService.loadUserByUsername(merchantResponse.getUsername());
        UserAccount userAccount = userService.getByContext();
        if (!userAccount.getId().equals(merchantAccount.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden, Access Denied");
        }
        return true;
    }
}
