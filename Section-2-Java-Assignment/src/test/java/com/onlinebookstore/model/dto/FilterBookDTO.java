package com.onlinebookstore.model.dto;

public class FilterBookDTO {

    private double minPrice;
    private double maxPrice;
    private Boolean availability;

    // Constructor
    public FilterBookDTO(double minPrice, double maxPrice, Boolean availability) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.availability = availability;
    }

    // Getters and Setters

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public boolean getAvail() {
        return availability;
    }

    public void setAvail(boolean availability) {
        this.availability = availability;
    }


}
