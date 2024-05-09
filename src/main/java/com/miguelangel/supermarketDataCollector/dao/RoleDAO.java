package com.miguelangel.supermarketDataCollector.dao;

import com.miguelangel.supermarketDataCollector.entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Transactional
public interface RoleDAO extends CrudRepository<Role, Integer> {

    @Query("SELECT r FROM Role r WHERE r.name = :roleName")
    Optional<Role> findByName(@Param("roleName") String roleName);
}
