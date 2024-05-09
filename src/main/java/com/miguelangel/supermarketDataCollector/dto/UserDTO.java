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

    public UserDTO() {
    }

    public UserDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.name = userEntity.getName();
        this.lastName = userEntity.getLastName();
        this.email = userEntity.getEmail();
        this.phone = userEntity.getPhone();
		this.createdAt = userEntity.getCreatedAt();
		this.lastConnection = userEntity.getLastConnection();
		if(!userEntity.getFavouriteProducts().isEmpty()){
			favouriteProductsId = userEntity.getFavouriteProducts().stream().map(Product::getId).collect(Collectors.toSet());
		}
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Set<String> getFavouriteProductsId() {
		return favouriteProductsId;
	}

	public void setFavouriteProductsId(Set<String> favouriteProductsId) {
		this.favouriteProductsId = favouriteProductsId;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getLastConnection() {
		return lastConnection;
	}

	public void setLastConnection(LocalDate lastConnection) {
		this.lastConnection = lastConnection;
	}
}
