package org.example.repository;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.model.Ad;
import org.example.model.CarAd;
import org.example.model.PetAd;
import org.example.model.RealEstateAd;
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
        String query =
                "SELECT id, description, location, price, type, brand, name, age, breed, surface FROM (" +
                        "    SELECT a.id, a.description, a.location, a.price, 'car' AS type, c.brand, NULL::TEXT AS name, NULL::INT AS age, NULL::TEXT AS breed, NULL::DOUBLE PRECISION AS surface " +
                        "    FROM ad a JOIN car_ad c ON a.id = c.ad_id " +
                        "    UNION ALL " +
                        "    SELECT a.id, a.description, a.location, a.price, 'pet' AS type, NULL::TEXT AS brand, p.name, p.age, p.breed, NULL::DOUBLE PRECISION AS surface " +
                        "    FROM ad a JOIN pet_ad p ON a.id = p.ad_id " +
                        "    UNION ALL " +
                        "    SELECT a.id, a.description, a.location, a.price, 'real_estate' AS type, NULL::TEXT AS brand, NULL::TEXT AS name, NULL::INT AS age, NULL::TEXT AS breed, r.surface " +
                        "    FROM ad a JOIN real_estate_ad r ON a.id = r.ad_id " +
                        ") AS unified_ads";

        try (Statement statement = dbConnection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                JsonObject adJson = new JsonObject();
                adJson.addProperty("id", resultSet.getInt("id"));
                adJson.addProperty("type", resultSet.getString("type"));
                adJson.addProperty("description", resultSet.getString("description"));
                adJson.addProperty("location", resultSet.getString("location"));
                adJson.addProperty("price", resultSet.getInt("price"));

                String type = resultSet.getString("type");

                switch (type) {
                    case "car":
                        adJson.addProperty("brand", resultSet.getString("brand"));
                        break;
                    case "pet":
                        adJson.addProperty("name", resultSet.getString("name"));
                        adJson.addProperty("age", resultSet.getInt("age"));
                        adJson.addProperty("breed", resultSet.getString("breed"));
                        break;
                    case "real_estate":
                        adJson.addProperty("surface", resultSet.getDouble("surface"));
                        break;
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

        String insertAdQuery = "INSERT INTO ad (type, description, location, price) VALUES (?, ?, ?, ?) RETURNING id";

        try (
                PreparedStatement adStmt = dbConnection.prepareStatement(insertAdQuery)
        ) {
            adStmt.setString(1, newAd.getType());
            adStmt.setString(2, newAd.getDescription());
            adStmt.setString(3, newAd.getLocation());
            adStmt.setInt(4, newAd.getPrice());

            ResultSet rs = adStmt.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Failed to insert ad.");
            }

            int generatedId = rs.getInt("id");

            switch (newAd.getType()) {
                case "CAR":
                    saveCarAd((CarAd) newAd, generatedId);
                    break;
                case "PET":
                    savePetAd((PetAd) newAd, generatedId);
                    break;
                case "REAL-ESTATE":
                    saveRealEstateAd((RealEstateAd) newAd, generatedId);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown ad type: " + newAd.getType());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save ad: " + e.getMessage(), e);
        }
    }

    private void saveCarAd(CarAd carAd, int adId) throws SQLException {
        String query = "INSERT INTO car_ad (ad_id, brand) VALUES (?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, adId);
            stmt.setString(2, carAd.getBrand());
            stmt.executeUpdate();
        }
    }

    private void savePetAd(PetAd petAd, int adId) throws SQLException {
        String query = "INSERT INTO pet_ad (ad_id, name, age, breed) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, adId);
            stmt.setString(2, petAd.getName());
            stmt.setInt(3, petAd.getAge());
            stmt.setString(4, petAd.getBreed());
            stmt.executeUpdate();
        }
    }

    private void saveRealEstateAd(RealEstateAd realEstateAd, int adId) throws SQLException {
        String query = "INSERT INTO real_estate_ad (ad_id, surface) VALUES (?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, adId);
            stmt.setDouble(2, realEstateAd.getSurface());
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves an ad by its unique ID.
     *
     * @param id the unique ID of the ad
     * @return the ad with the specified ID, or {@code null} if no ad is found
     */
    public JsonObject getById(int id) {
        JsonObject adJson = new JsonObject();
        String query = "SELECT id, type, description, location, price FROM ad WHERE id = ?";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Ad with id " + id + " not found.");
                    return null;
                }

                String type = rs.getString("type");

                adJson.addProperty("id", rs.getInt("id"));
                adJson.addProperty("type", type);
                adJson.addProperty("description", rs.getString("description"));
                adJson.addProperty("location", rs.getString("location"));
                adJson.addProperty("price", rs.getInt("price"));

                switch (type) {
                    case "car":
                        fetchCarAdFields(id, adJson);
                        break;
                    case "pet":
                        fetchPetAdFields(id, adJson);
                        break;
                    case "real_estate":
                        fetchRealEstateAdFields(id, adJson);
                        break;
                    default:
                        System.out.println("Unknown ad type: " + type);
                }

                return adJson;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void fetchCarAdFields(int adId, JsonObject json) throws SQLException {
        String query = "SELECT brand FROM car_ad WHERE ad_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, adId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    json.addProperty("brand", rs.getString("brand"));
                }
            }
        }
    }

    private void fetchPetAdFields(int adId, JsonObject json) throws SQLException {
        String query = "SELECT name, age, breed FROM pet_ad WHERE ad_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, adId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    json.addProperty("name", rs.getString("name"));
                    json.addProperty("age", rs.getInt("age"));
                    json.addProperty("breed", rs.getString("breed"));
                }
            }
        }
    }

    private void fetchRealEstateAdFields(int adId, JsonObject json) throws SQLException {
        String query = "SELECT surface FROM real_estate_ad WHERE ad_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, adId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    json.addProperty("surface", rs.getDouble("surface"));
                }
            }
        }
    }

}
