package org.example.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.model.Ad;
import org.example.registry.Registry;
import org.example.repository.AdRepository;
import org.example.util.AdBuilder;

import java.util.List;
import java.util.Map;


/**
 * The {@code AdService} class is responsible for managing advertisement-related operations.
 * It provides methods for fetching all ads, fetching an ad by ID, and saving a new ad.
 * <p>
 * This service interacts with the {@link AdRepository} to retrieve and store ads, and utilizes the
 * {@link Registry} to determine the correct ad class based on the ad type. The {@link AdBuilder} is used
 * to build the concrete {@link Ad} instance from the provided field values.
 */
public class AdService {
    private final AdRepository adRepository = new AdRepository();

    /**
     * Retrieves all advertisements from the repository.
     * <p>
     * This method queries the {@link AdRepository} to retrieve a list of all ads and returns them
     * as a JSON string. The JSON is generated using the {@link Gson} library.
     *
     * @return a JSON string containing all ads in the repository
     */
    public String getAll() {
        List<JsonObject> adsList = adRepository.getAll();

        Gson gson = new Gson();
        return gson.toJson(adsList);
    }

    /**
     * Retrieves a specific advertisement by its ID.
     * <p>
     * This method queries the {@link AdRepository} to retrieve an ad by its ID and returns it
     * as a JSON string. The JSON is generated using the {@link Gson} library.
     *
     * @param id the ID of the advertisement to retrieve
     * @return a JSON string containing the ad with the given ID
     */
    public String getById(int id) {
        JsonObject ad = adRepository.getById(id);
        Gson gson = new Gson();
        return gson.toJson(ad);
    }

    /**
     * Saves a new advertisement to the repository.
     * <p>
     * This method accepts a map of field-value pairs representing the attributes of the ad.
     * The method checks if the ad type exists in the {@link Registry}, and if so, it uses
     * the {@link AdBuilder} to create an instance of the corresponding ad class. The new ad
     * is then saved in the {@link AdRepository}.
     *
     * @param fieldValueMap a map of field names and values representing the advertisement attributes
     * @throws IllegalArgumentException if the provided ad type is invalid or not found in the registry
     */
    public void save(Map<String, Object> fieldValueMap) {
        String adType = (String) fieldValueMap.get("type");
        Class<? extends Ad> adClass = Registry.getInstance().getConcreteAdClass(adType.toUpperCase());
        if(adClass != null) {
            Ad newAd = AdBuilder.buildAd(adClass, fieldValueMap);
            adRepository.save(newAd);
        }else {
            throw new IllegalArgumentException("Invalid ad type: " + fieldValueMap.get("type"));
        }
    }
}
