package com.miguelangel.supermarketDataCollector.services;

import com.miguelangel.supermarketDataCollector.entity.Role;

import java.util.Optional;

/**
 * Interface defining operations available for managing roles.
 *
 *  @since 2024
 *  @author Miguel Angel Moreno Garcia
 */
public interface IRoleService {

    /**
     * Finds a role by its name.
     *
     * @param roleName The name of the role to find.
     * @return An Optional containing the found Role, or empty if not found.
     */
    Optional<Role> findByName(String roleName);
}
