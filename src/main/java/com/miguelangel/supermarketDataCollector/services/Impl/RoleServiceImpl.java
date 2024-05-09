package com.miguelangel.supermarketDataCollector.services.Impl;

import com.miguelangel.supermarketDataCollector.dao.RoleDAO;
import com.miguelangel.supermarketDataCollector.entity.Role;
import com.miguelangel.supermarketDataCollector.services.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    RoleDAO roleDAO;

    @Override
    public Optional<Role> findByName(String roleName) {
        return roleDAO.findByName(roleName);
    }
}
