package com.miguelangel.supermarketDataCollector.dto;

import com.miguelangel.supermarketDataCollector.entity.Product;
import com.miguelangel.supermarketDataCollector.entity.UserEntity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Data Transfer Object (DTO) representing a user.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
public class UserDTO implements Serializable {

    @Serial
	private static final long serialVersionUID = 1L;

	private String token;
    private int id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
	private LocalDate createdAt;
	private LocalDate lastConnection;
	private Set<String> favouriteProductsId = new HashSet<>(0);

	/**
	 * Default constructor for UserDTO.
	 */
	public UserDTO() {
	}

	/**
	 * Constructor for UserDTO with a UserEntity parameter.
	 *
	 * @param userEntity The UserEntity from which to create the DTO
	 */
	public UserDTO(UserEntity userEntity) {
		this.id = userEntity.getId();
		this.name = userEntity.getName();
		this.lastName = userEntity.getLastName();
		this.email = userEntity.getEmail();
		this.phone = userEntity.getPhone();
		this.createdAt = userEntity.getCreatedAt();
		this.lastConnection = userEntity.getLastConnection();
		if (!userEntity.getFavouriteProducts().isEmpty()) {
			favouriteProductsId = userEntity.getFavouriteProducts().stream().map(Product::getId).collect(Collectors.toSet());
		}
	}

	/**
	 * Getter method for the user's token.
	 *
	 * @return The token associated with the user
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Setter method for the user's token.
	 *
	 * @param token The token to be set for the user
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Getter method for the user's ID.
	 *
	 * @return The ID of the user
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter method for the user's ID.
	 *
	 * @param id The ID to be set for the user
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getter method for the user's name.
	 *
	 * @return The name of the user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the user's name.
	 *
	 * @param name The name to be set for the user
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter method for the user's last name.
	 *
	 * @return The last name of the user
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Setter method for the user's last name.
	 *
	 * @param lastName The last name to be set for the user
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Getter method for the user's email.
	 *
	 * @return The email of the user
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setter method for the user's email.
	 *
	 * @param email The email to be set for the user
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Getter method for the user's phone number.
	 *
	 * @return The phone number of the user
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Setter method for the user's phone number.
	 *
	 * @param phone The phone number to be set for the user
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Getter method for the IDs of the user's favourite products.
	 *
	 * @return The set of IDs of the user's favourite products
	 */
	public Set<String> getFavouriteProductsId() {
		return favouriteProductsId;
	}

	/**
	 * Setter method for the IDs of the user's favourite products.
	 *
	 * @param favouriteProductsId The set of IDs to be set as the user's favourite products
	 */
	public void setFavouriteProductsId(Set<String> favouriteProductsId) {
		this.favouriteProductsId = favouriteProductsId;
	}

	/**
	 * Getter method for the creation date of the user.
	 *
	 * @return The creation date of the user's account
	 */
	public LocalDate getCreatedAt() {
		return createdAt;
	}

	/**
	 * Setter method for the creation date of the user.
	 *
	 * @param createdAt The creation date to be set for the user's account
	 */
	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Getter method for the last connection date of the user.
	 *
	 * @return The date of the user's last connection
	 */
	public LocalDate getLastConnection() {
		return lastConnection;
	}

	/**
	 * Setter method for the last connection date of the user.
	 *
	 * @param lastConnection The date of the user's last connection to be set
	 */
	public void setLastConnection(LocalDate lastConnection) {
		this.lastConnection = lastConnection;
	}

}
