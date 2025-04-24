package org.example.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for loading and accessing environment properties from a properties file.
 * <p>
 * This class loads the environment-specific configurations, such as database credentials,
 * from a file named {@code .properties} located in the project's root directory.
 * The values are then stored as static constants, which can be accessed via the class itself.
 * </p>
 *
 * <p>
 * Properties expected to be available in the file include:
 * - {@code DBNAME}: The name of the database to connect to.
 * - {@code DBUSER}: The username for database access.
 * - {@code DBPASSWORD}: The password for the database user.
 * </p>
 */
public class Environment {
    private static final Properties properties = new Properties();

    static {
        try {
            Environment.properties.load(new FileInputStream(String.format("%s/%s", System.getProperty("user.dir"), ".properties")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String DBNAME = properties.getProperty("DBNAME");
    public static final String DBUSER = properties.getProperty("DBUSER");
    public static final String DBPASSWORD = properties.getProperty("DBPASSWORD");
}
