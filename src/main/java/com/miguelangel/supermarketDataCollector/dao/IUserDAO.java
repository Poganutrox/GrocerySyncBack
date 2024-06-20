package com.miguelangel.supermarketDataCollector.dao;

import com.miguelangel.supermarketDataCollector.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on UserEntity entities.
 */
public interface IUserDAO extends CrudRepository<UserEntity, Integer> {

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user to find
     * @return an Optional containing the user with the specified email address, or an empty Optional if no user is found
     */
    @Transactional
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);
}
