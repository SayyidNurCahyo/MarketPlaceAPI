package com.enigma.marketplace.MarketPlaceAPI.service;

import com.enigma.marketplace.MarketPlaceAPI.constant.UserRole;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.AuthRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.NewProductRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.RegisterMerchantRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.RegisterRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.LoginResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.RegisterResponse;
import com.enigma.marketplace.MarketPlaceAPI.entity.*;
import com.enigma.marketplace.MarketPlaceAPI.repository.UserAccountRepository;
import com.enigma.marketplace.MarketPlaceAPI.service.implement.AuthServiceImpl;
import com.enigma.marketplace.MarketPlaceAPI.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomerService customerService;
    @Mock
    private JwtService jwtService;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private MerchantService merchantService;
    private AuthService authService;
    @BeforeEach
    void setUp(){
        authService =new AuthServiceImpl(userAccountRepository, roleService, passwordEncoder, customerService, jwtService, validationUtil, authenticationManager, merchantService);
    }

    @Test
    void registerCustomer() {
        RegisterRequest request = RegisterRequest.builder()
                .username("testuser")
                .password("testpassword")
                .name("Test User")
                .phone("1234567890")
                .build();
        Role role = Role.builder()
                .id("1")
                .role(UserRole.ROLE_CUSTOMER)
                .build();
        UserAccount account = UserAccount.builder()
                .id("1")
                .username("testuser")
                .password("testpassword")
                .isEnable(true)
                .roles(List.of(role))
                .build();
        when(roleService.getOrSave(UserRole.ROLE_CUSTOMER)).thenReturn(role);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedpassword");
        when(userAccountRepository.saveAndFlush(any(UserAccount.class))).thenReturn(account);
        doNothing().when(customerService).addCustomer(any(Customer.class));
        RegisterResponse response = authService.registerCustomer(request);
        assertEquals("testuser", response.getUsername());
        assertEquals(List.of("ROLE_CUSTOMER"), response.getRoles());
        verify(validationUtil).validate(request);
        verify(roleService).getOrSave(UserRole.ROLE_CUSTOMER);
        verify(passwordEncoder).encode(request.getPassword());
        verify(userAccountRepository).saveAndFlush(any(UserAccount.class));
        verify(customerService).addCustomer(any(Customer.class));
    }

    @Test
    void registerMerchant() {
        RegisterMerchantRequest request = RegisterMerchantRequest.builder()
                .username("testuser")
                .password("testpassword")
                .name("Test User")
                .phone("1234567890")
                .address("testAddress")
                .products(List.of(NewProductRequest.builder().name("productName")
                        .price(1L).stock(1).build()))
                .build();
        Role role = Role.builder()
                .id("1")
                .role(UserRole.ROLE_MERCHANT)
                .build();
        UserAccount account = UserAccount.builder()
                .id("1")
                .username("testuser")
                .password("testpassword")
                .isEnable(true)
                .roles(List.of(role))
                .build();
        when(roleService.getOrSave(UserRole.ROLE_MERCHANT)).thenReturn(role);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedpassword");
        when(userAccountRepository.saveAndFlush(any(UserAccount.class))).thenReturn(account);
        doNothing().when(merchantService).addMerchant(any(Merchant.class));
        RegisterResponse response = authService.registerMerchant(request);
        assertEquals("testuser", response.getUsername());
        assertEquals(List.of("ROLE_MERCHANT"), response.getRoles());
        verify(validationUtil).validate(request);
        verify(roleService).getOrSave(UserRole.ROLE_MERCHANT);
        verify(passwordEncoder).encode(request.getPassword());
        verify(userAccountRepository).saveAndFlush(any(UserAccount.class));
        verify(merchantService).addMerchant(any(Merchant.class));
    }

    @Test
    void login() {
        AuthRequest request = new AuthRequest("username", "password");
        UserAccount userAccount = UserAccount.builder().id("tes account id")
                .password("password").username("username")
                .isEnable(true).roles(List.of(Role.builder().id("tes role id")
                        .role(UserRole.ROLE_CUSTOMER).build())).build();
        String token = "token";
        LoginResponse expectedResponse = LoginResponse.builder()
                .username("username")
                .roles(List.of("ROLE_CUSTOMER"))
                .token(token)
                .build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(new UsernamePasswordAuthenticationToken(userAccount, null));
        when(jwtService.generateToken(userAccount)).thenReturn(token);
        LoginResponse actualResponse = authService.login(request);
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(userAccount);
    }
}
