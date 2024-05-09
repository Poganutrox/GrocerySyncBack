package com.miguelangel.supermarketDataCollector.services;

import java.util.List;
import java.util.Optional;

import com.miguelangel.supermarketDataCollector.entity.UserEntity;

/**
 * Interface defining operations available for managing users.
 * <p>
 * Author: Miguel Ángel Moreno García
 */
public interface IUserService {

    /**
     * Finds a user by their unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return An Optional that may contain the found user, or empty if not found.
     */
    Optional<UserEntity> findById(int id);

    /**
     * Finds users by their unique identifiers.
     *
     * @param ids List of user unique identifiers.
     * @return List of found users.
     */
    List<UserEntity> findByIds(List<Integer> ids);

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user.
     * @return An Optional that may contain the found user, or empty if not found.
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Saves a userEntity in the system.
     *
     * @param userEntity The userEntity to save.
     * @return The saved userEntity.
     */
    UserEntity save(UserEntity userEntity);
}
