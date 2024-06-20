package com.miguelangel.supermarketDataCollector.entity;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * Represents a role within the supermarket data collection system.
 * This class is mapped to the "role" table in the database.
 * Each role has a unique identifier and a name.
 *
 * @since 2024
 * @author Miguel Angel Moreno Garcia
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    /**
     * Returns the name of the role.
     *
     * @return the name of the role
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the role.
     *
     * @param name the name of the role
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the ID of the role.
     *
     * @return the ID of the role
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the role.
     *
     * @param id the ID of the role
     */
    public void setId(int id) {
        this.id = id;
    }
}
