package com.miguelangel.supermarketDataCollector.services.Impl;

import com.miguelangel.supermarketDataCollector.dao.RoleDAO;
import com.miguelangel.supermarketDataCollector.entity.Role;
import com.miguelangel.supermarketDataCollector.services.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 * This class implements the IRoleService interface and provides methods to interact with Role entities.
 * It is responsible for handling operations related to roles in the application.
 *
 *  @since 2024
 *  @author Miguel Angel Moreno Garcia
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    RoleDAO roleDAO;

    /**
     * Retrieves an optional Role entity by its name.
     *
     * @param roleName the name of the role to search for.
     * @return an Optional containing the Role entity if found, otherwise an empty Optional.
     */
    @Override
    public Optional<Role> findByName(String roleName) {
        return roleDAO.findByName(roleName);
    }

}
