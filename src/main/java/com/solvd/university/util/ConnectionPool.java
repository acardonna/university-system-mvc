package com.solvd.university.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger();
    private static ConnectionPool instance;

    private final BlockingQueue<Connection> availableConnections;
    private final BlockingQueue<Connection> usedConnections;
    private final String url;
    private final String username;
    private final String password;
    private final int poolSize;
    private final int timeout;

    private ConnectionPool() {
        Properties properties = loadDatabaseProperties();

        this.url = properties.getProperty("db.url");
        this.username = properties.getProperty("db.username");
        this.password = properties.getProperty("db.password");
        this.poolSize = Integer.parseInt(properties.getProperty("db.pool.size", "10"));
        this.timeout = Integer.parseInt(properties.getProperty("db.pool.timeout", "30000"));

        this.availableConnections = new ArrayBlockingQueue<>(poolSize);
        this.usedConnections = new ArrayBlockingQueue<>(poolSize);

        initializePool();

        // LOGGER.info("Connection pool initialized with {} connections", poolSize);

    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    private Properties loadDatabaseProperties() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                LOGGER.error("Unable to find database.properties");
                throw new RuntimeException("database.properties not found");
            }
            properties.load(input);
        } catch (IOException e) {
            LOGGER.error("Error loading database properties", e);
            throw new RuntimeException("Failed to load database configuration", e);
        }
        return properties;
    }

    private void initializePool() {
        for (int i = 0; i < poolSize; i++) {
            try {
                Connection connection = createConnection();
                availableConnections.add(connection);
            } catch (SQLException e) {
                LOGGER.error("Failed to create connection {}", i + 1, e);
                throw new RuntimeException("Failed to initialize connection pool", e);
            }
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public Connection getConnection() throws SQLException {
        try {
            Connection connection = availableConnections.poll(timeout, TimeUnit.MILLISECONDS);

            if (connection == null) {
                throw new SQLException("Timeout waiting for available connection");
            }

            if (!connection.isValid(2)) {
                LOGGER.warn("Invalid connection detected, creating new one");
                connection = createConnection();
            }

            usedConnections.add(connection);
            LOGGER.debug(
                "Connection retrieved from pool. Available: {}, Used: {}",
                availableConnections.size(),
                usedConnections.size()
            );

            return connection;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Interrupted while waiting for connection", e);
        }
    }

    public boolean releaseConnection(Connection connection) {
        if (connection == null) {
            return false;
        }

        if (usedConnections.remove(connection)) {
            try {
                if (connection.isClosed()) {
                    LOGGER.warn("Attempting to release closed connection, creating new one");
                    connection = createConnection();
                }

                availableConnections.add(connection);
                LOGGER.debug(
                    "Connection returned to pool. Available: {}, Used: {}",
                    availableConnections.size(),
                    usedConnections.size()
                );
                return true;
            } catch (SQLException e) {
                LOGGER.error("Error checking connection status", e);
                return false;
            }
        }

        return false;
    }

    public String getPoolStats() {
        return String.format(
            "Pool Stats - Available: %d, Used: %d, Total: %d",
            availableConnections.size(),
            usedConnections.size(),
            poolSize
        );
    }

    public void shutdown() {
        LOGGER.info("Shutting down connection pool");

        usedConnections.forEach(this::closeConnection);
        availableConnections.forEach(this::closeConnection);

        usedConnections.clear();
        availableConnections.clear();

        LOGGER.info("Connection pool shutdown complete");
    }

    private void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error("Error closing connection", e);
        }
    }

    public int getPoolSize() {
        return poolSize;
    }

    public int getAvailableConnectionsCount() {
        return availableConnections.size();
    }

    public int getUsedConnectionsCount() {
        return usedConnections.size();
    }
}
