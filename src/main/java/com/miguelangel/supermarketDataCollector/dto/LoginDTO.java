package com.miguelangel.supermarketDataCollector.dto;

/**
 * Data Transfer Object (DTO) for transferring login information.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
public class LoginDTO {
    private String email;
    private String password;

    /**
     * Retrieves the email of the user.
     *
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
