package org.example.model;

import javax.inject.Named;

/**
 * The {@code RealEstateAd} class represents a real estate advertisement.
 * It extends the {@code Ad} class and adds a field for the surface in square meters of the property.
 */
public class RealEstateAd extends Ad {
    private final double surface;

    public RealEstateAd(String type, String description, String location, int price, @Named("surface") double surface) {
        super(type, description, location, price);
        this.surface = surface;
    }

    public double getSurface() {
        return surface;
    }
}

