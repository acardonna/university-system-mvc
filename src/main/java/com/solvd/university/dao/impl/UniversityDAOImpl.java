package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.UniversityDAO;
import com.solvd.university.model.University;
import com.solvd.university.util.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UniversityDAOImpl implements UniversityDAO {

    private static final Logger LOGGER = LogManager.getLogger(UniversityDAOImpl.class);
    private final ConnectionPool connectionPool;

    public UniversityDAOImpl() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public void save(University university) {
        String sql = "INSERT INTO university (name) VALUES (?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, university.getName());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    university.setUniversityId(generatedKeys.getInt(1));
                    LOGGER.info("University saved successfully with ID: {}", university.getUniversityId());
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to save university: {}", university.getName(), e);
            throw new RuntimeException("Failed to save university", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error("Failed to close statement", e);
                }
            }
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public Optional<University> findById(Integer universityId) {
        String sql = "SELECT * FROM university WHERE university_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, universityId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                University university = mapResultSetToUniversity(rs);
                return Optional.of(university);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to find university by ID: {}", universityId, e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOGGER.error("Failed to close result set", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error("Failed to close statement", e);
                }
            }
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<University> findByName(String name) {
        String sql = "SELECT * FROM university WHERE name = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            rs = stmt.executeQuery();

            if (rs.next()) {
                University university = mapResultSetToUniversity(rs);
                return Optional.of(university);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to find university by name: {}", name, e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOGGER.error("Failed to close result set", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error("Failed to close statement", e);
                }
            }
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<University> findAll() {
        String sql = "SELECT * FROM university";

        List<University> universities = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                University university = mapResultSetToUniversity(rs);
                universities.add(university);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to retrieve all universities", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOGGER.error("Failed to close result set", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error("Failed to close statement", e);
                }
            }
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return universities;
    }

    @Override
    public void update(University university) {
        String sql = "UPDATE university SET name = ? WHERE university_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, university.getName());
            stmt.setInt(2, university.getUniversityId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("University updated successfully: {}", university.getName());
            } else {
                LOGGER.warn("No university found with ID: {}", university.getUniversityId());
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to update university: {}", university.getName(), e);
            throw new RuntimeException("Failed to update university", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error("Failed to close statement", e);
                }
            }
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void delete(Integer universityId) {
        String sql = "DELETE FROM university WHERE university_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, universityId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("University deleted successfully with ID: {}", universityId);
            } else {
                LOGGER.warn("No university found with ID: {}", universityId);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to delete university with ID: {}", universityId, e);
            throw new RuntimeException("Failed to delete university", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.error("Failed to close statement", e);
                }
            }
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    private University mapResultSetToUniversity(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        University university = new University(name);
        university.setUniversityId(rs.getInt("university_id"));
        return university;
    }
}
