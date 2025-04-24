package org.example.repository;

import org.example.model.Ad;
import org.example.util.DatabaseConnectionUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code TypeRepository} class is responsible for interacting with the {@code Type} table in the database.
 * It provides functionality to retrieve all advertisement types stored in the database.
 */
public class TypeRepository {
    private final Connection dbConnection = DatabaseConnectionUtil.connect();

    /**
     * Retrieves all advertisement types from the database.
     * <p>
     * This method queries the {@code Type} table to get a list of all type names and returns them as a list of
     * strings. Each string represents the name of a type.
     * </p>
     *
     * @return a list of all advertisement type names stored in the {@code Type} table
     */
    public ArrayList<String> getAll() {
        ArrayList<String> types = new ArrayList<>();

        String query = "SELECT name FROM Type";
        try (Statement statement = dbConnection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String typeName = resultSet.getString("name");
                types.add(typeName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return types;
    }
}