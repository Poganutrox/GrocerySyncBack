package com.miguelangel.supermarketDataCollector.services.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.miguelangel.supermarketDataCollector.dao.IUserDAO;
import com.miguelangel.supermarketDataCollector.entity.UserEntity;
import com.miguelangel.supermarketDataCollector.services.IUserService;

/**
 * Implementation of the IUserService interface that provides operations for managing users.
 * <p>
 * Author: Miguel Ángel Moreno García
 */
@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	IUserDAO userDAO;

	/**
	 * Retrieves users by their unique identifiers.
	 *
	 * @param ids The list of user unique identifiers.
	 * @return List of found users.
	 */
	@Override
	public List<UserEntity> findByIds(List<Integer> ids) {
		return (List<UserEntity>) userDAO.findAllById(ids);
	}

	/**
	 * Retrieves a user by their unique identifier.
	 *
	 * @param id The unique identifier of the user.
	 * @return An Optional that may contain the found user, or empty if not found.
	 */
	@Override
	public Optional<UserEntity> findById(int id) {
		return userDAO.findById(id);
	}

	/**
	 * Retrieves a user by their email address.
	 *
	 * @param email The email address of the user.
	 * @return An Optional containing the found user, or empty if not found.
	 */
	@Override
	public Optional<UserEntity> findByEmail(String email) {
		return userDAO.findByEmail(email);
	}

	/**
	 * Saves a userEntity in the system.
	 *
	 * @param userEntity The userEntity to save.
	 * @return The saved userEntity.
	 */
	@Override
	public UserEntity save(UserEntity userEntity) {
		return userDAO.save(userEntity);
	}

}
