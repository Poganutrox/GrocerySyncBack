package com.miguelangel.supermarketDataCollector.services;

import com.miguelangel.supermarketDataCollector.entity.Role;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(String roleName);
}
