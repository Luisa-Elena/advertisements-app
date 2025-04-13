package org.example.repository;

import org.example.model.Ad;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * The {@code AdRepository} class acts as a simple in-memory repository for storing and managing
 * advertisements. It uses a {@link ConcurrentHashMap} to store ads with their unique IDs, and
 * provides methods to retrieve, save, and fetch ads by ID.
 *
 * This class is thread-safe due to the usage of {@link ConcurrentHashMap} for storing ads
 * and {@link AtomicInteger} for generating unique IDs.
 */
public class AdRepository {
    public static volatile ConcurrentHashMap<Integer, Ad> ads = new ConcurrentHashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    /**
     * Retrieves all ads stored in the repository.
     *
     * @return a list of all ads
     */
    public List<Ad> getAll() {
        return new ArrayList<>(ads.values());
    }

    /**
     * Saves a new advertisement to the repository.
     *
     * This method generates a unique ID for the new ad and adds it to the repository.
     *
     * @param newAd the ad to be saved
     * @throws IllegalArgumentException if the ad is {@code null}
     */
    public void save(Ad newAd) {
        if (newAd == null) {
            throw new IllegalArgumentException("Cannot save a null ad.");
        }

        int id = idGenerator.getAndIncrement();
        ads.put(id, newAd);
    }

    /**
     * Retrieves an ad by its unique ID.
     *
     * @param id the unique ID of the ad
     * @return the ad with the specified ID, or {@code null} if no ad is found
     */
    public Ad getById(int id) {
        return ads.get(id);
    }
}
