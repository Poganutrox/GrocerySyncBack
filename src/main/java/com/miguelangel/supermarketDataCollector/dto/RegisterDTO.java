package com.miguelangel.supermarketDataCollector.dto;

/**
 * Data Transfer Object (DTO) for user registration.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
public class RegisterDTO {

    private String name;
    private String lastName;
    private String email;
    private String password;
    private String phone;

    /**
     * Retrieves the user's first name.
     *
     * @return the user's first name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's first name.
     *
     * @param name the user's first name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the user's last name.
     *
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName the user's last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Retrieves the user's email address.
     *
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the user's email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the user's password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password the user's password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the user's phone number.
     *
     * @return the user's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the user's phone number.
     *
     * @param phone the user's phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}

