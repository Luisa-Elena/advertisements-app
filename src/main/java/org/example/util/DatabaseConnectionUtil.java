package org.example.util;

import org.example.config.Environment;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Utility class for managing database connections to a PostgreSQL server.
 * <p>
 * This class provides a static method, {@link #connect()}, that establishes a connection to a PostgreSQL
 * database using configuration values from the {@code Environment} class. The connection details, including the
 * database name, username, and password, are retrieved from environment variables or configuration files.
 * </p>
 *
 * <p>
 * It is intended to be used for handling database connections in a centralized manner, making it easier to manage
 * connections across the application.
 * </p>
 */
public class DatabaseConnectionUtil {
    /**
     * Establishes a connection to the PostgreSQL database using credentials from the {@code Environment} class.
     *
     * @return a {@link Connection} object if the connection is successfully established, or {@code null} otherwise.
     * @throws RuntimeException if the connection attempt fails or if the database connection cannot be established.
     */
    public static Connection connect() {

        String dbName = Environment.DBNAME;
        String user = Environment.DBUSER;
        String password = Environment.DBPASSWORD;
        String url = "jdbc:postgresql://localhost:5432/" + dbName;


        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn= DriverManager.getConnection(url, user, password);
            if(conn == null) {
                throw new Exception("Failed to connect to database.");
            } else {
                System.out.println("Connection established.");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}
