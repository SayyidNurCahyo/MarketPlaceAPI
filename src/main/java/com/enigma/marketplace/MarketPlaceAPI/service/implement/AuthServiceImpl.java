package com.enigma.marketplace.MarketPlaceAPI.service.implement;

import com.enigma.marketplace.MarketPlaceAPI.constant.UserRole;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.AuthRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.request.RegisterRequest;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.LoginResponse;
import com.enigma.marketplace.MarketPlaceAPI.dto.response.RegisterResponse;
import com.enigma.marketplace.MarketPlaceAPI.entity.Customer;
import com.enigma.marketplace.MarketPlaceAPI.entity.Role;
import com.enigma.marketplace.MarketPlaceAPI.entity.UserAccount;
import com.enigma.marketplace.MarketPlaceAPI.repository.UserAccountRepository;
import com.enigma.marketplace.MarketPlaceAPI.service.AuthService;
import com.enigma.marketplace.MarketPlaceAPI.service.CustomerService;
import com.enigma.marketplace.MarketPlaceAPI.service.JwtService;
import com.enigma.marketplace.MarketPlaceAPI.service.RoleService;
import com.enigma.marketplace.MarketPlaceAPI.util.ValidationUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserAccountRepository userAccountRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final JwtService jwtService;
    private final ValidationUtil validationUtil;
    private final AuthenticationManager authenticationManager;
    private final AdminService adminService;

    @Value("${wmbapi.username.superadmin}")
    private String superAdminUsername;
    @Value("${wmbapi.password.superadmin}")
    private String superAdminPassword;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initAdmin() {
        Optional<UserAccount> currentUser = userAccountRepository.findByUsername(superAdminUsername);
        if (currentUser.isPresent()) return;
        Role superAdmin = roleService.getOrSave(UserRole.ROLE_ADMIN);
        Role admin = roleService.getOrSave(UserRole.ROLE_ADMIN);
        Role customer = roleService.getOrSave(UserRole.ROLE_CUSTOMER);
        UserAccount account = UserAccount.builder()
                .username(superAdminUsername)
                .password(passwordEncoder.encode(superAdminPassword))
                .roles(List.of(superAdmin, admin, customer))
                .isEnable(true)
                .build();
        userAccountRepository.save(account);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerCustomer(RegisterRequest request) throws DataIntegrityViolationException {
        validationUtil.validate(request);
        Role role=roleService.getOrSave(UserRole.ROLE_CUSTOMER);
        String hashPassword = passwordEncoder.encode(request.getPassword());
        UserAccount account = UserAccount.builder()
                .username(request.getUsername())
                .password(hashPassword)
                .roles(List.of(role))
                .isEnable(true).build();
        userAccountRepository.saveAndFlush(account);
        Customer customer = Customer.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .userAccount(account).build();
        customerService.addCustomer(customer);
        List<String> roleAuth = account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return RegisterResponse.builder()
                .username(account.getUsername())
                .roles(roleAuth).build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerMerchant(RegisterRequest request) throws DataIntegrityViolationException {
        validationUtil.validate(request);
        Role role=roleService.getOrSave(UserRole.ROLE_ADMIN);
        String hashPassword = passwordEncoder.encode(request.getPassword());
        UserAccount account = UserAccount.builder()
                .username(request.getUsername())
                .password(hashPassword)
                .roles(List.of(role))
                .isEnable(true).build();
        userAccountRepository.saveAndFlush(account);
        Admin admin = Admin.builder().name(request.getName())
                .phone(request.getPhone()).userAccount(account).build();
        adminService.addAdmin(admin);
        List<String> roleAuth = account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return RegisterResponse.builder()
                .username(account.getUsername())
                .roles(roleAuth).build();
    }

    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(AuthRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserAccount userAccount = (UserAccount) authenticate.getPrincipal();
        String token = jwtService.generateToken(userAccount);
        return LoginResponse.builder()
                .username(userAccount.getUsername())
                .roles(userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .token(token)
                .build();
    }

    public boolean validateToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) this.userAccountRepository.findByUsername(authentication.getPrincipal().toString()).orElse(null);
        return userAccount != null;
    }
}
