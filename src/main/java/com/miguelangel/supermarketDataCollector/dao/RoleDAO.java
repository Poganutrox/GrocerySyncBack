package com.miguelangel.supermarketDataCollector.dao;

import com.miguelangel.supermarketDataCollector.entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on Role entities.
 */
@Transactional
public interface RoleDAO extends CrudRepository<Role, Integer> {

    /**
     * Finds a role by its name.
     *
     * @param roleName the name of the role to find
     * @return an Optional containing the role with the specified name, or an empty Optional if no role is found
     */
    @Query("SELECT r FROM Role r WHERE r.name = :roleName")
    Optional<Role> findByName(@Param("roleName") String roleName);
}
