//package com.enigma.marketplace.MarketPlaceAPI.security;
//
//import com.enigma.marketplace.MarketPlaceAPI.entity.Customer;
//import com.enigma.marketplace.MarketPlaceAPI.entity.UserAccount;
//import com.enigma.marketplace.MarketPlaceAPI.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ResponseStatusException;
//
//@Component
//@RequiredArgsConstructor
//public class AuthenticatedUser {
//    private final CustomerService customerService;
//    private final UserService userService;
//    public boolean hasId(String customerId){
//        Customer customerResponse = customerService.getCustomerById(customerId);
//        UserAccount customerAccount = (UserAccount) userService.loadUserByUsername(customerResponse.getCustomerUsername());
//        UserAccount userAccount = userService.getByContext();
//        if (!userAccount.getId().equals(customerAccount.getId())) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden, Access Denied");
//        }
//        return true;
//    }
//}
