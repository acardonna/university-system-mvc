package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.CourseDifficultyDAO;
import com.solvd.university.model.CourseDifficulty;
import com.solvd.university.util.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CourseDifficultyDAOImpl implements CourseDifficultyDAO {

    private static final Logger LOGGER = LogManager.getLogger(CourseDifficultyDAOImpl.class);
    private final ConnectionPool connectionPool;

    public CourseDifficultyDAOImpl() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public Optional<CourseDifficulty> findById(Integer courseDifficultyId) {
        String sql = "SELECT * FROM course_difficulty WHERE course_difficulty_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseDifficultyId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCourseDifficulty(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding course difficulty by ID: {}", courseDifficultyId, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<CourseDifficulty> findByDisplayName(String displayName) {
        String sql = "SELECT * FROM course_difficulty WHERE display_name = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, displayName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCourseDifficulty(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding course difficulty by display name: {}", displayName, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<CourseDifficulty> findByLevel(int level) {
        String sql = "SELECT * FROM course_difficulty WHERE lvl = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, level);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCourseDifficulty(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding course difficulty by level: {}", level, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<CourseDifficulty> findAll() {
        String sql = "SELECT * FROM course_difficulty ORDER BY lvl";
        List<CourseDifficulty> difficulties = new ArrayList<>();
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                difficulties.add(mapResultSetToCourseDifficulty(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error retrieving all course difficulties", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return difficulties;
    }

    @Override
    public void save(CourseDifficulty difficulty) {
        String sql = "INSERT INTO course_difficulty (display_name, lvl) VALUES (?, ?)";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, difficulty.getDisplayName());
            stmt.setInt(2, difficulty.getLevel());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                difficulty.setCourseDifficultyId(rs.getInt(1));
                LOGGER.info("Course difficulty saved successfully with ID: {}", difficulty.getCourseDifficultyId());
            }
        } catch (SQLException e) {
            LOGGER.error("Error saving course difficulty: {}", difficulty.getDisplayName(), e);
            throw new RuntimeException("Failed to save course difficulty", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void update(CourseDifficulty difficulty) {
        String sql = "UPDATE course_difficulty SET display_name = ?, lvl = ? WHERE course_difficulty_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, difficulty.getDisplayName());
            stmt.setInt(2, difficulty.getLevel());
            stmt.setInt(3, difficulty.getCourseDifficultyId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Course difficulty updated successfully with ID: {}", difficulty.getCourseDifficultyId());
            } else {
                LOGGER.warn("No course difficulty found with ID: {}", difficulty.getCourseDifficultyId());
            }
        } catch (SQLException e) {
            LOGGER.error("Error updating course difficulty: {}", difficulty.getCourseDifficultyId(), e);
            throw new RuntimeException("Failed to update course difficulty", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void delete(Integer courseDifficultyId) {
        String sql = "DELETE FROM course_difficulty WHERE course_difficulty_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseDifficultyId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Course difficulty deleted successfully with ID: {}", courseDifficultyId);
            } else {
                LOGGER.warn("No course difficulty found with ID: {}", courseDifficultyId);
            }
        } catch (SQLException e) {
            LOGGER.error("Error deleting course difficulty: {}", courseDifficultyId, e);
            throw new RuntimeException("Failed to delete course difficulty", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    private CourseDifficulty mapResultSetToCourseDifficulty(ResultSet rs) throws SQLException {
        CourseDifficulty difficulty = new CourseDifficulty();
        difficulty.setCourseDifficultyId(rs.getInt("course_difficulty_id"));
        difficulty.setDisplayName(rs.getString("display_name"));
        difficulty.setLevel(rs.getInt("lvl"));
        return difficulty;
    }
}
