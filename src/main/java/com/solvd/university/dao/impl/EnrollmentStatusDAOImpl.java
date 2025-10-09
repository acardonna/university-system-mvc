package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.EnrollmentStatusDAO;
import com.solvd.university.model.EnrollmentStatus;
import com.solvd.university.util.ConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnrollmentStatusDAOImpl implements EnrollmentStatusDAO {

    private static final Logger LOGGER = LogManager.getLogger(EnrollmentStatusDAOImpl.class);
    private final ConnectionPool connectionPool;

    public EnrollmentStatusDAOImpl() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public Optional<EnrollmentStatus> findById(Integer enrollmentStatusId) {
        String sql = "SELECT * FROM enrollment_status WHERE enrollment_status_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, enrollmentStatusId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToEnrollmentStatus(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding enrollment status by ID: {}", enrollmentStatusId, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<EnrollmentStatus> findByDisplayName(String displayName) {
        String sql = "SELECT * FROM enrollment_status WHERE display_name = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, displayName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToEnrollmentStatus(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding enrollment status by display name: {}", displayName, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<EnrollmentStatus> findAll() {
        String sql = "SELECT * FROM enrollment_status ORDER BY enrollment_status_id";
        List<EnrollmentStatus> statuses = new ArrayList<>();
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                statuses.add(mapResultSetToEnrollmentStatus(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error retrieving all enrollment statuses", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return statuses;
    }

    @Override
    public void save(EnrollmentStatus status) {
        String sql = "INSERT INTO enrollment_status (display_name, description) VALUES (?, ?)";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, status.getDisplayName());
            stmt.setString(2, status.getDescription());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                status.setEnrollmentStatusId(rs.getInt(1));
                LOGGER.info("Enrollment status saved successfully with ID: {}", status.getEnrollmentStatusId());
            }
        } catch (SQLException e) {
            LOGGER.error("Error saving enrollment status: {}", status.getDisplayName(), e);
            throw new RuntimeException("Failed to save enrollment status", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void update(EnrollmentStatus status) {
        String sql = "UPDATE enrollment_status SET display_name = ?, description = ? WHERE enrollment_status_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, status.getDisplayName());
            stmt.setString(2, status.getDescription());
            stmt.setInt(3, status.getEnrollmentStatusId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Enrollment status updated successfully with ID: {}", status.getEnrollmentStatusId());
            } else {
                LOGGER.warn("No enrollment status found with ID: {}", status.getEnrollmentStatusId());
            }
        } catch (SQLException e) {
            LOGGER.error("Error updating enrollment status: {}", status.getEnrollmentStatusId(), e);
            throw new RuntimeException("Failed to update enrollment status", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void delete(Integer enrollmentStatusId) {
        String sql = "DELETE FROM enrollment_status WHERE enrollment_status_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, enrollmentStatusId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Enrollment status deleted successfully with ID: {}", enrollmentStatusId);
            } else {
                LOGGER.warn("No enrollment status found with ID: {}", enrollmentStatusId);
            }
        } catch (SQLException e) {
            LOGGER.error("Error deleting enrollment status: {}", enrollmentStatusId, e);
            throw new RuntimeException("Failed to delete enrollment status", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    private EnrollmentStatus mapResultSetToEnrollmentStatus(ResultSet rs) throws SQLException {
        EnrollmentStatus status = new EnrollmentStatus();
        status.setEnrollmentStatusId(rs.getInt("enrollment_status_id"));
        status.setDisplayName(rs.getString("display_name"));
        return status;
    }
}
