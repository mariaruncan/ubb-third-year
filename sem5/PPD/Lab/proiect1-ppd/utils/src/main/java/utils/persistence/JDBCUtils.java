package utils.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {
    private Properties properties;
    private Connection instance = null;

    public JDBCUtils(Properties properties) {
        this.properties = properties;
    }

    public Connection getConnection() {
        try {
            if (instance == null || instance.isClosed())
                instance = getNewConnection();
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return instance;
    }

    private Connection getNewConnection() {
        String driver = properties.getProperty("server.jdbc.driver");
        String url = properties.getProperty("server.jdbc.url");
        String user = properties.getProperty("server.jdbc.user");
        String password = properties.getProperty("server.jdbc.password");

        Connection connection = null;
        try {
            Class.forName(driver);
            if (user != null && password != null)
                connection = DriverManager.getConnection(url, user, password);
            else
                connection = DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading driver " + e);
        } catch (SQLException e) {
            System.out.println("Error getting connection " + e);
        }
        return connection;
    }
}
