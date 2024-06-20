package com.miguelangel.supermarketDataCollector.model;

import com.miguelangel.supermarketDataCollector.entity.Supermarket;

/**
 * This class provides static instances of Supermarket objects representing various supermarkets.
 *
 *  @since 2024
 *  @author Miguel Angel Moreno Garcia
 */
public class Supermarkets {
    /**
     * Represents the Mercadona supermarket.
     */
    public static final Supermarket MERCADONA = new Supermarket(1, "Mercadona", "https://www.mercadona.es");

    /**
     * Represents the Día supermarket.
     */
    public static final Supermarket DIA = new Supermarket(2, "Dia", "https://www.dia.es");

    /**
     * Represents the Alcampo supermarket.
     */
    public static final Supermarket ALCAMPO = new Supermarket(3, "Alcampo", "https://www.compraonline.alcampo.es");

    /**
     * Represents the Consum supermarket.
     */
    public static final Supermarket CONSUM = new Supermarket(4, "Consum", "https://www.consum.es");

}
