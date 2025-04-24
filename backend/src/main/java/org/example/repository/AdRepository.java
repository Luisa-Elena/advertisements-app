package org.example.repository;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.model.Ad;
import org.example.util.DatabaseConnectionUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@code AdRepository} class provides methods to interact with the database
 * for storing and retrieving {@code Ad} objects. It supports operations such as
 * fetching all ads, saving a new ad, and fetching a specific ad by its ID.
 */
public class AdRepository {
    public static volatile ConcurrentHashMap<Integer, Ad> ads = new ConcurrentHashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private final Connection dbConnection = DatabaseConnectionUtil.connect();

    /**
     * Retrieves all ads stored in the database.
     *
     * @return a list of all ads
     */
    public List<JsonObject> getAll() {
        List<JsonObject> adList = new ArrayList<>();
        String query = "SELECT a.id, a.description, a.price, a.spec, a.location, t.name AS type_name " +
                "FROM Ad a " +
                "JOIN Type t ON a.type = t.id";

        try (Statement statement = dbConnection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                JsonObject adJson = new JsonObject();
                adJson.addProperty("id", resultSet.getInt("id"));
                adJson.addProperty("type", resultSet.getString("type_name"));
                adJson.addProperty("description", resultSet.getString("description"));
                adJson.addProperty("price", resultSet.getInt("price"));
                adJson.addProperty("location", resultSet.getString("location"));

                String spec = resultSet.getString("spec");
                JsonElement specJson = JsonParser.parseString(spec);

                if (specJson.isJsonObject()) {
                    JsonObject specObj = specJson.getAsJsonObject();
                    for (String key : specObj.keySet()) {
                        adJson.add(key, specObj.get(key));
                    }
                }

                adList.add(adJson);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return adList;
    }

    /**
     * Inserts a new advertisement into the database.
     * <p>
     * The ad's type, description, price, location, and specifications (as JSON) are stored.
     * The type is matched by name to get its corresponding ID from the {@code Type} table.
     * </p>
     *
     * @param newAd the {@code Ad} object to be saved. Must not be {@code null}.
     * @throws IllegalArgumentException if {@code newAd} is {@code null}.
     * @throws RuntimeException if an error occurs during the database insertion.
     */
    public void save(Ad newAd) {
        if (newAd == null) {
            throw new IllegalArgumentException("Cannot save a null ad.");
        }

        String query = "INSERT INTO Ad (type, description, price, location, spec) VALUES " +
                       "((SELECT id FROM Type WHERE name = ?), ?, ?, ?, ?::jsonb)";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            Map<String, Object> specMap = new HashMap<>();
            Class<? extends Ad> newAdClass = newAd.getClass();
            for(Field field : newAdClass.getDeclaredFields()) {
                field.setAccessible(true);
                specMap.put(field.getName(), field.get(newAd));
            }

            Gson gson = new Gson();
            String spec = gson.toJson(specMap);

            pstmt.setString(1, newAd.getType());
            pstmt.setString(2, newAd.getDescription());
            pstmt.setInt(3, newAd.getPrice());
            pstmt.setString(4, newAd.getLocation());
            pstmt.setString(5, spec);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted == 0) {
                throw new RuntimeException("Insert failed, no rows affected.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to save ad: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves an ad by its unique ID.
     *
     * @param id the unique ID of the ad
     * @return the ad with the specified ID, or {@code null} if no ad is found
     */
    public JsonObject getById(int id) {
        String query = "SELECT a.id, a.description, a.price, a.spec, a.location, t.name AS type_name " +
                "FROM Ad a " +
                "JOIN Type t ON a.type = t.id " +
                "WHERE a.id = ?";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    JsonObject adJson = new JsonObject();
                    adJson.addProperty("id", resultSet.getInt("id"));
                    adJson.addProperty("type", resultSet.getString("type_name"));
                    adJson.addProperty("description", resultSet.getString("description"));
                    adJson.addProperty("price", resultSet.getInt("price"));
                    adJson.addProperty("location", resultSet.getString("location"));

                    String spec = resultSet.getString("spec");
                    JsonElement specJson = JsonParser.parseString(spec);

                    if (specJson.isJsonObject()) {
                        JsonObject specObj = specJson.getAsJsonObject();
                        for (String key : specObj.keySet()) {
                            adJson.add(key, specObj.get(key));
                        }
                    }

                    return adJson;
                } else {
                    System.out.println("Ad with id " + id + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
