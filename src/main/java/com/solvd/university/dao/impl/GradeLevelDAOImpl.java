package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.GradeLevelDAO;
import com.solvd.university.model.GradeLevel;
import com.solvd.university.util.ConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GradeLevelDAOImpl implements GradeLevelDAO {

    private static final Logger LOGGER = LogManager.getLogger(GradeLevelDAOImpl.class);
    private final ConnectionPool connectionPool;

    public GradeLevelDAOImpl() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public Optional<GradeLevel> findById(Integer gradeLevelId) {
        String sql = "SELECT * FROM grade_level WHERE grade_level_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gradeLevelId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToGradeLevel(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding grade level by ID: {}", gradeLevelId, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<GradeLevel> findByDisplayName(String displayName) {
        String sql = "SELECT * FROM grade_level WHERE display_name = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, displayName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToGradeLevel(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding grade level by display name: {}", displayName, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<GradeLevel> findByYear(int year) {
        String sql = "SELECT * FROM grade_level WHERE year = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, year);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToGradeLevel(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding grade level by year: {}", year, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<GradeLevel> findAll() {
        String sql = "SELECT * FROM grade_level ORDER BY year";
        List<GradeLevel> gradeLevels = new ArrayList<>();
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                gradeLevels.add(mapResultSetToGradeLevel(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error retrieving all grade levels", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return gradeLevels;
    }

    @Override
    public void save(GradeLevel gradeLevel) {
        String sql = "INSERT INTO grade_level (display_name, year) VALUES (?, ?)";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, gradeLevel.getDisplayName());
            stmt.setInt(2, gradeLevel.getYear());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                gradeLevel.setGradeLevelId(rs.getInt(1));
                LOGGER.info("Grade level saved successfully with ID: {}", gradeLevel.getGradeLevelId());
            }
        } catch (SQLException e) {
            LOGGER.error("Error saving grade level: {}", gradeLevel.getDisplayName(), e);
            throw new RuntimeException("Failed to save grade level", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void update(GradeLevel gradeLevel) {
        String sql = "UPDATE grade_level SET display_name = ?, year = ? WHERE grade_level_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, gradeLevel.getDisplayName());
            stmt.setInt(2, gradeLevel.getYear());
            stmt.setInt(3, gradeLevel.getGradeLevelId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Grade level updated successfully with ID: {}", gradeLevel.getGradeLevelId());
            } else {
                LOGGER.warn("No grade level found with ID: {}", gradeLevel.getGradeLevelId());
            }
        } catch (SQLException e) {
            LOGGER.error("Error updating grade level: {}", gradeLevel.getGradeLevelId(), e);
            throw new RuntimeException("Failed to update grade level", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void delete(Integer gradeLevelId) {
        String sql = "DELETE FROM grade_level WHERE grade_level_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gradeLevelId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Grade level deleted successfully with ID: {}", gradeLevelId);
            } else {
                LOGGER.warn("No grade level found with ID: {}", gradeLevelId);
            }
        } catch (SQLException e) {
            LOGGER.error("Error deleting grade level: {}", gradeLevelId, e);
            throw new RuntimeException("Failed to delete grade level", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    private GradeLevel mapResultSetToGradeLevel(ResultSet rs) throws SQLException {
        GradeLevel gradeLevel = new GradeLevel();
        gradeLevel.setGradeLevelId(rs.getInt("grade_level_id"));
        gradeLevel.setDisplayName(rs.getString("display_name"));
        gradeLevel.setYear(rs.getInt("year"));
        return gradeLevel;
    }
}
