package org.example.repository;

import org.example.model.Ad;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code TypeRepository} class is responsible for storing and providing the available advertisement types.
 * It contains a static list of predefined advertisement types. These types must also be registered in the
 *  * {@link org.example.registry.Registry} so that there is a concrete class available for each ad type.
 */
public class TypeRepository {
    public static ArrayList<String> types = new ArrayList<>();

    static {
        types.add("CAR");
        types.add("REAL-ESTATE");
        types.add("PET");
    }

    public ArrayList<String> getAll() {
        return types;
    }
}