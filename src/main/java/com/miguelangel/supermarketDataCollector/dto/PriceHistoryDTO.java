package com.miguelangel.supermarketDataCollector.dto;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.miguelangel.supermarketDataCollector.entity.PriceHistory;

public class PriceHistoryDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String date;
    private BigDecimal price;
    private BigDecimal salePrice;
    private BigDecimal bulkPrice;

    public PriceHistoryDTO() {
    }
    
    public PriceHistoryDTO(PriceHistory pricehistory) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.date = pricehistory.getId().getDate().format(formatter);
        this.price = pricehistory.getPrice();
        this.salePrice = pricehistory.getSalePrice();
        this.bulkPrice = pricehistory.getBulkPrice();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getBulkPrice() {
        return bulkPrice;
    }

    public void setBulkPrice(BigDecimal bulkPrice) {
        this.bulkPrice = bulkPrice;
    }
}

