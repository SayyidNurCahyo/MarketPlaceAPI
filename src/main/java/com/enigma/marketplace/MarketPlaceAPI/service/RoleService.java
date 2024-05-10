package com.enigma.marketplace.MarketPlaceAPI.service;

import com.enigma.marketplace.MarketPlaceAPI.constant.UserRole;
import com.enigma.marketplace.MarketPlaceAPI.entity.Role;

public interface RoleService {
    Role getOrSave(UserRole role);
}
