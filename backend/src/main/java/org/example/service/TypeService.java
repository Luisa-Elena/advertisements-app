package org.example.service;

import com.google.gson.Gson;
import org.example.repository.TypeRepository;

import java.util.ArrayList;

/**
 * The {@code TypeService} class handles the business logic for managing ad types.
 * It retrieves ad types from the repository and converts them to JSON format for API responses.
 */
public class TypeService {

    private final TypeRepository typeRepository = new TypeRepository();
    public String getAll() {
        ArrayList<String> types = typeRepository.getAll();

        Gson gson = new Gson();
        return gson.toJson(types);
    }
}
