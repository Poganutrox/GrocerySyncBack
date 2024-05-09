package com.miguelangel.supermarketDataCollector.dao;

import com.miguelangel.supermarketDataCollector.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface IUserDAO extends CrudRepository<UserEntity, Integer> {

    @Transactional
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);
}
