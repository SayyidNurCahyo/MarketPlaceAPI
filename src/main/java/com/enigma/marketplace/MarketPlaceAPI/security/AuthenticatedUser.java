package com.enigma.marketplace.MarketPlaceAPI.security;

import com.enigma.wmbapi.constant.ResponseMessage;
import com.enigma.wmbapi.dto.response.CustomerResponse;
import com.enigma.wmbapi.entity.UserAccount;
import com.enigma.wmbapi.service.CustomerService;
import com.enigma.wmbapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class AuthenticatedUser {
    private final CustomerService customerService;
    private final UserService userService;
    public boolean hasId(String customerId){
        CustomerResponse customerResponse = customerService.getCustomerById(customerId);
        UserAccount customerAccount = (UserAccount) userService.loadUserByUsername(customerResponse.getCustomerUsername());
        UserAccount userAccount = userService.getByContext();
        if (!userAccount.getId().equals(customerAccount.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ResponseMessage.ERROR_FORBIDDEN);
        }
        return true;
    }
}
