package com.miguelangel.supermarketDataCollector.dto;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.miguelangel.supermarketDataCollector.entity.PriceHistory;

/**
 * Data Transfer Object (DTO) for transferring price history information.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
public class PriceHistoryDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String date;
    private BigDecimal price;
    private BigDecimal salePrice;
    private BigDecimal bulkPrice;

    /**
     * Default constructor.
     */
    public PriceHistoryDTO() {
    }

    /**
     * Constructs a PriceHistoryDTO from a PriceHistory entity.
     *
     * @param pricehistory the PriceHistory entity
     */
    public PriceHistoryDTO(PriceHistory pricehistory) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.date = pricehistory.getId().getDate().format(formatter);
        this.price = pricehistory.getPrice();
        this.salePrice = pricehistory.getSalePrice();
        this.bulkPrice = pricehistory.getBulkPrice();
    }

    /**
     * Retrieves the date of the price history.
     *
     * @return the date of the price history
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the price history.
     *
     * @param date the date of the price history
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Retrieves the price of the product.
     *
     * @return the price of the product
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the price of the product
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Retrieves the sale price of the product.
     *
     * @return the sale price of the product
     */
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    /**
     * Sets the sale price of the product.
     *
     * @param salePrice the sale price of the product
     */
    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    /**
     * Retrieves the bulk price of the product.
     *
     * @return the bulk price of the product
     */
    public BigDecimal getBulkPrice() {
        return bulkPrice;
    }

    /**
     * Sets the bulk price of the product.
     *
     * @param bulkPrice the bulk price of the product
     */
    public void setBulkPrice(BigDecimal bulkPrice) {
        this.bulkPrice = bulkPrice;
    }
}
