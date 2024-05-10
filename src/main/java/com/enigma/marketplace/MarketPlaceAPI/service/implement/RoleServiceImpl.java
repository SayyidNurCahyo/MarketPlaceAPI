package com.enigma.marketplace.MarketPlaceAPI.service.implement;

import com.enigma.marketplace.MarketPlaceAPI.constant.UserRole;
import com.enigma.marketplace.MarketPlaceAPI.entity.Role;
import com.enigma.marketplace.MarketPlaceAPI.repository.RoleRepository;
import com.enigma.marketplace.MarketPlaceAPI.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Role getOrSave(UserRole role) {
        return roleRepository.findByRole(role).orElseGet(()->roleRepository.saveAndFlush(Role.builder().role(role).build()));
    }
}
