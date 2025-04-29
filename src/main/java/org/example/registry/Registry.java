package org.example.registry;

import org.example.model.Ad;
import org.example.model.CarAd;
import org.example.model.PetAd;
import org.example.model.RealEstateAd;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code Registry} class is a singleton that maintains a mapping between ad types
 * (represented as strings) and their corresponding concrete {@code Ad} class implementations.
 * This class is used to retrieve the correct concrete class based on an ad's type.
 */
public class Registry {
    private static Registry instance = null;
    private final Map<String, Class<? extends Ad>> adMap = new HashMap<>();

    private Registry() {
        initializeAds();
    }

    public static synchronized Registry getInstance() {
        if (instance == null) {
            instance = new Registry();
        }
        return instance;
    }

    /**
     * Initializes the {@code adMap} with predefined ad types and their corresponding concrete classes.
     * This method is called only once when the {@code Registry} is created.
     */
    private void initializeAds() {
        adMap.put("REAL-ESTATE", RealEstateAd.class);
        adMap.put("CAR", CarAd.class);
        adMap.put("PET", PetAd.class);
    }

    /**
     * Returns the concrete {@code Ad} class corresponding to the given ad type.
     *
     * @param type the type of the ad (e.g., "REAL-ESTATE", "CAR", "PET")
     * @return the concrete class corresponding to the given ad type, or {@code null} if the type is not registered
     */
    public Class<? extends Ad> getConcreteAdClass(String type) {
        return adMap.get(type);
    }
}
