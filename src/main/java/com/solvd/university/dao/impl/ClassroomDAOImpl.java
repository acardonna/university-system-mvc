package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.BuildingDAO;
import com.solvd.university.dao.interfaces.ClassroomDAO;
import com.solvd.university.model.Building;
import com.solvd.university.model.Classroom;
import com.solvd.university.util.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClassroomDAOImpl implements ClassroomDAO {

    private static final Logger LOGGER = LogManager.getLogger(ClassroomDAOImpl.class);
    private final ConnectionPool connectionPool;
    private final BuildingDAO buildingDAO;

    public ClassroomDAOImpl(BuildingDAO buildingDAO) {
        this.connectionPool = ConnectionPool.getInstance();
        this.buildingDAO = buildingDAO;
    }

    @Override
    public void save(Classroom classroom) {
        String sql =
            "INSERT INTO classroom (room_number, building_id, capacity, room_type, " +
            "scheduled_start, scheduled_end, university_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, classroom.getRoomNumber());

            if (classroom.getBuildingId() != null) {
                stmt.setInt(2, classroom.getBuildingId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setInt(3, classroom.getCapacity());
            stmt.setString(4, classroom.getRoomType());

            if (classroom.getScheduledStart() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(classroom.getScheduledStart()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }

            if (classroom.getScheduledEnd() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(classroom.getScheduledEnd()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }

            if (classroom.getUniversityId() != null) {
                stmt.setInt(7, classroom.getUniversityId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    classroom.setClassroomId(generatedKeys.getInt(1));
                    LOGGER.info("Classroom saved successfully with ID: {}", classroom.getClassroomId());
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to save classroom: {}", classroom.getRoomNumber(), e);
            throw new RuntimeException("Failed to save classroom", e);
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
    public Optional<Classroom> findById(Integer classroomId) {
        String sql = "SELECT * FROM classroom WHERE classroom_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, classroomId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Classroom classroom = mapResultSetToClassroom(rs);
                return Optional.of(classroom);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to find classroom by id: {}", classroomId, e);
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
    public Optional<Classroom> findByRoomNumber(String roomNumber) {
        String sql = "SELECT * FROM classroom WHERE room_number = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, roomNumber);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Classroom classroom = mapResultSetToClassroom(rs);
                return Optional.of(classroom);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to find classroom by room number: {}", roomNumber, e);
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
    public List<Classroom> findAll() {
        String sql = "SELECT * FROM classroom";

        List<Classroom> classrooms = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Classroom classroom = mapResultSetToClassroom(rs);
                classrooms.add(classroom);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to retrieve all classrooms", e);
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
        return classrooms;
    }

    @Override
    public void update(Classroom classroom) {
        String sql =
            "UPDATE classroom SET building_id = ?, capacity = ?, room_type = ?, " +
            "scheduled_start = ?, scheduled_end = ?, university_id = ? WHERE room_number = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);

            if (classroom.getBuildingId() != null) {
                stmt.setInt(1, classroom.getBuildingId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            stmt.setInt(2, classroom.getCapacity());
            stmt.setString(3, classroom.getRoomType());

            if (classroom.getScheduledStart() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(classroom.getScheduledStart()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }

            if (classroom.getScheduledEnd() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(classroom.getScheduledEnd()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }

            if (classroom.getUniversityId() != null) {
                stmt.setInt(6, classroom.getUniversityId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.setString(7, classroom.getRoomNumber());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Classroom updated successfully: {}", classroom.getRoomNumber());
            } else {
                LOGGER.warn("No classroom found with room number: {}", classroom.getRoomNumber());
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to update classroom: {}", classroom.getRoomNumber(), e);
            throw new RuntimeException("Failed to update classroom", e);
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
    public void delete(Integer classroomId) {
        String sql = "DELETE FROM classroom WHERE classroom_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, classroomId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Classroom deleted successfully with ID: {}", classroomId);
            } else {
                LOGGER.warn("No classroom found with ID: {}", classroomId);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to delete classroom: {}", classroomId, e);
            throw new RuntimeException("Failed to delete classroom", e);
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

    private Classroom mapResultSetToClassroom(ResultSet rs) throws SQLException {
        Classroom classroom = new Classroom();

        classroom.setClassroomId(rs.getInt("classroom_id"));
        classroom.setRoomNumber(rs.getString("room_number"));

        Integer buildingId = rs.getObject("building_id", Integer.class);
        classroom.setBuildingId(buildingId);

        Integer universityId = rs.getObject("university_id", Integer.class);
        classroom.setUniversityId(universityId);

        classroom.setCapacity(rs.getInt("capacity"));
        classroom.setRoomType(rs.getString("room_type"));

        Timestamp startTimestamp = rs.getTimestamp("scheduled_start");
        if (startTimestamp != null) {
            classroom.schedule(
                startTimestamp.toLocalDateTime(),
                rs.getTimestamp("scheduled_end").toLocalDateTime(),
                null
            );
        }

        if (buildingId != null) {
            Optional<Building> building = buildingDAO.findById(buildingId);
            building.ifPresent(classroom::setBuilding);
            if (building.isEmpty()) {
                LOGGER.warn(
                    "Classroom {} references non-existent building_id {}",
                    classroom.getRoomNumber(),
                    buildingId
                );
            }
        }

        return classroom;
    }
}
