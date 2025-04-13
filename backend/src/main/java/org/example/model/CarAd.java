package org.example.model;

import javax.inject.Named;

/**
 * The {@code CarAd} class represents a car advertisement.
 * It extends the {@code Ad} class and adds a field for the car's brand.
 */
public class CarAd extends Ad {
    private final String brand;

    public CarAd(String type, String description, String location, int price, @Named("brand") String brand) {
        super(type, description, location, price);
        this.brand = brand;
    }
}
