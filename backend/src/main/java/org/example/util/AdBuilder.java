package org.example.util;

import org.example.model.Ad;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * The {@code AdBuilder} class is responsible for dynamically creating instances of {@code Ad}
 * subclasses based on the provided class type and field values.
 * It uses reflection to identify constructors and match the required parameters.
 */
public class AdBuilder {

    /**
     * Builds an instance of the specified ad class using the provided field values.
     * It uses reflection to match constructor parameters with the values in the fieldValueMap.
     *
     * @param adClass         The class type of the ad to be created (e.g., {@code CarAd}, {@code RealEstateAd}).
     * @param fieldValueMap   A map of field names and their corresponding values used to populate the ad.
     * @return               An instance of the specified ad type, populated with the values from fieldValueMap.
     * @throws IllegalArgumentException If any required field is missing or if the constructor parameters do not match.
     * @throws RuntimeException         If no suitable constructor is found or if instantiation fails.
     */
    public static Ad buildAd(Class<? extends Ad> adClass, Map<String, Object> fieldValueMap) {
        try {
            Constructor<?>[] constructors = adClass.getConstructors();

            for(Constructor<?> constructor : constructors) {
                Parameter[] constructorParams = constructor.getParameters();
                Object[] args = new Object[constructorParams.length];

                for (int i = 0; i < constructorParams.length; i++) {
                    Parameter param = constructorParams[i];
                    String paramName = param.getName();
                    Class<?> paramType = param.getType();

                    Object valueObj = fieldValueMap.get(paramName);

                    if (valueObj == null) {
                        throw new IllegalArgumentException("Missing required field: " + paramName);
                    }

                    if (paramType == int.class || paramType == Integer.class) {
                        args[i] = ((Number) valueObj).intValue();
                    } else if (paramType == double.class || paramType == Double.class) {
                        args[i] = ((Number) valueObj).doubleValue();
                    } else if (paramType == String.class) {
                        args[i] = valueObj;
                    } else if (paramType == boolean.class || paramType == Boolean.class) {
                        args[i] = valueObj;
                    }
                }

                return (Ad) constructor.newInstance(args);
            }
            throw new RuntimeException("No suitable constructor found for class: " + adClass.getSimpleName());
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
