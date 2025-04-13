package org.example.model;

import javax.inject.Named;

/**
 * The {@code Ad} class is an abstract class that represents a generic advertisement.
 * It includes fields for the advertisement's type, description, location, and price.
 * This class serves as a base class for different types of advertisements (e.g., car, real estate, pet).
 */
public abstract class Ad {
    private final String type;
    private final String description;
    private final String location;
    private final int price;

    public Ad(@Named("type") String type, @Named("description") String description, @Named("location") String location, @Named("price") int price) {
        this.type = type;
        this.description = description;
        this.location = location;
        this.price = price;
    }
}
