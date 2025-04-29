package org.example.model;

import javax.inject.Named;

/**
 * The {@code PetAd} class represents a pet advertisement.
 * It extends the {@code Ad} class and adds fields for the pet's name, age and breed.
 */
public class PetAd extends Ad {
    private String name;
    private int age;
    private String breed;

    public PetAd(String type, String description, String location, int price, @Named("name") String name, @Named("age") int age, @Named("breed") String breed) {
        super(type, description, location, price);
        this.name = name;
        this.age = age;
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getBreed() {
        return breed;
    }
}
