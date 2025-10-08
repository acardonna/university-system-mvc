package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.BuildingDAO;
import com.solvd.university.model.Building;
import com.solvd.university.util.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuildingDAOImpl implements BuildingDAO {

    private static final Logger LOGGER = LogManager.getLogger(BuildingDAOImpl.class);
    private final ConnectionPool connectionPool;

    public BuildingDAOImpl() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public void save(Building building) {
        String sql = "INSERT INTO building (name, university_id) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, building.getName());

            if (building.getUniversityId() != null) {
                stmt.setInt(2, building.getUniversityId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    building.setBuildingId(generatedKeys.getInt(1));
                    LOGGER.info("Building saved successfully with ID: {}", building.getBuildingId());
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to save building: {}", building.getName(), e);
            throw new RuntimeException("Failed to save building", e);
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
    public Optional<Building> findById(Integer buildingId) {
        String sql = "SELECT * FROM building WHERE building_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, buildingId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Building building = mapResultSetToBuilding(rs);
                return Optional.of(building);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to find building by ID: {}", buildingId, e);
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
    public Optional<Building> findByName(String name) {
        String sql = "SELECT * FROM building WHERE name = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Building building = mapResultSetToBuilding(rs);
                return Optional.of(building);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to find building by name: {}", name, e);
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
    public List<Building> findAll() {
        String sql = "SELECT * FROM building";

        List<Building> buildings = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Building building = mapResultSetToBuilding(rs);
                buildings.add(building);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to retrieve all buildings", e);
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
        return buildings;
    }

    @Override
    public List<Building> findByUniversity(Integer universityId) {
        String sql = "SELECT * FROM building WHERE university_id = ?";

        List<Building> buildings = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, universityId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Building building = mapResultSetToBuilding(rs);
                buildings.add(building);
            }
            LOGGER.info("Found {} buildings for university ID: {}", buildings.size(), universityId);
        } catch (SQLException e) {
            LOGGER.error("Failed to retrieve buildings for university ID: {}", universityId, e);
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
        return buildings;
    }

    @Override
    public void update(Building building) {
        String sql = "UPDATE building SET name = ?, university_id = ? WHERE building_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, building.getName());

            if (building.getUniversityId() != null) {
                stmt.setInt(2, building.getUniversityId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setInt(3, building.getBuildingId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Building updated successfully: {}", building.getName());
            } else {
                LOGGER.warn("No building found with ID: {}", building.getBuildingId());
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to update building: {}", building.getName(), e);
            throw new RuntimeException("Failed to update building", e);
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
    public void delete(Integer buildingId) {
        String sql = "DELETE FROM building WHERE building_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, buildingId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Building deleted successfully with ID: {}", buildingId);
            } else {
                LOGGER.warn("No building found with ID: {}", buildingId);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to delete building with ID: {}", buildingId, e);
            throw new RuntimeException("Failed to delete building", e);
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

    private Building mapResultSetToBuilding(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        Building building = new Building(name);
        building.setBuildingId(rs.getInt("building_id"));
        building.setUniversityId(rs.getObject("university_id", Integer.class));
        return building;
    }
}
