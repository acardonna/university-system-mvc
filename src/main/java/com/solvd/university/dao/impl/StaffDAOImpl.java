package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.StaffDAO;
import com.solvd.university.model.ConcreteStaff;
import com.solvd.university.model.Staff;
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

public class StaffDAOImpl implements StaffDAO {

    private static final Logger LOGGER = LogManager.getLogger(StaffDAOImpl.class);
    private final ConnectionPool connectionPool;

    public StaffDAOImpl() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public void save(Staff staff) {
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            conn.setAutoCommit(false);

            String personSql = "INSERT INTO person (first_name, last_name, email) VALUES (?, ?, ?)";
            PreparedStatement personStmt = conn.prepareStatement(personSql, Statement.RETURN_GENERATED_KEYS);
            personStmt.setString(1, staff.getFirstName());
            personStmt.setString(2, staff.getLastName());
            personStmt.setString(3, staff.getEmail());
            personStmt.executeUpdate();

            ResultSet personRs = personStmt.getGeneratedKeys();
            Integer personId = null;
            if (personRs.next()) {
                personId = personRs.getInt(1);
                staff.setPersonId(personId);
            }

            String staffSql = "INSERT INTO staff (department_id, title, person_id) VALUES (?, ?, ?)";
            PreparedStatement staffStmt = conn.prepareStatement(staffSql, Statement.RETURN_GENERATED_KEYS);
            staffStmt.setObject(1, staff.getDepartmentId());
            staffStmt.setString(2, staff.getTitle());
            staffStmt.setInt(3, personId);
            staffStmt.executeUpdate();

            ResultSet staffRs = staffStmt.getGeneratedKeys();
            if (staffRs.next()) {
                staff.setStaffId(staffRs.getInt(1));
            }

            String updatePersonSql = "UPDATE person SET staff_id = ? WHERE person_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updatePersonSql);
            updateStmt.setInt(1, staff.getStaffId());
            updateStmt.setInt(2, personId);
            updateStmt.executeUpdate();

            conn.commit();
            LOGGER.debug(
                "Staff member saved: {} (staff_id={}, person_id={})",
                staff.getFullName(),
                staff.getStaffId(),
                personId
            );
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    LOGGER.error("Error rolling back transaction", rollbackEx);
                }
            }
            LOGGER.error("Error saving staff member: {}", staff.getFullName(), e);
            throw new RuntimeException("Failed to save staff member", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.error("Error resetting autocommit", e);
                }
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public Optional<Staff> findById(Integer staffId) {
        String sql =
            "SELECT s.*, p.first_name, p.last_name, p.email " +
            "FROM staff s " +
            "JOIN person p ON s.person_id = p.person_id " +
            "WHERE s.staff_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, staffId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToStaff(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding staff by ID: {}", staffId, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Staff> findByPersonId(Integer personId) {
        String sql =
            "SELECT s.*, p.first_name, p.last_name, p.email " +
            "FROM staff s " +
            "JOIN person p ON s.person_id = p.person_id " +
            "WHERE p.person_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, personId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToStaff(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding staff by person ID: {}", personId, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Staff> findAll() {
        String sql =
            "SELECT s.*, p.first_name, p.last_name, p.email " +
            "FROM staff s " +
            "JOIN person p ON s.person_id = p.person_id";
        List<Staff> staffMembers = new ArrayList<>();
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                staffMembers.add(mapResultSetToStaff(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error retrieving all staff members", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return staffMembers;
    }

    @Override
    public List<Staff> findByDepartment(Integer departmentId) {
        String sql =
            "SELECT s.*, p.first_name, p.last_name, p.email " +
            "FROM staff s " +
            "JOIN person p ON s.person_id = p.person_id " +
            "WHERE s.department_id = ?";
        List<Staff> staffMembers = new ArrayList<>();
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                staffMembers.add(mapResultSetToStaff(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error retrieving staff members by department ID: {}", departmentId, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return staffMembers;
    }

    @Override
    public void update(Staff staff) {
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            conn.setAutoCommit(false);

            String personSql = "UPDATE person SET first_name = ?, last_name = ?, email = ? WHERE person_id = ?";
            PreparedStatement personStmt = conn.prepareStatement(personSql);
            personStmt.setString(1, staff.getFirstName());
            personStmt.setString(2, staff.getLastName());
            personStmt.setString(3, staff.getEmail());
            personStmt.setInt(4, staff.getPersonId());
            personStmt.executeUpdate();

            String staffSql = "UPDATE staff SET department_id = ?, title = ? WHERE staff_id = ?";
            PreparedStatement staffStmt = conn.prepareStatement(staffSql);
            staffStmt.setObject(1, staff.getDepartmentId());
            staffStmt.setString(2, staff.getTitle());
            staffStmt.setInt(3, staff.getStaffId());
            staffStmt.executeUpdate();

            conn.commit();
            LOGGER.debug("Staff member updated: {}", staff.getFullName());
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    LOGGER.error("Error rolling back transaction", rollbackEx);
                }
            }
            LOGGER.error("Error updating staff member: {}", staff.getFullName(), e);
            throw new RuntimeException("Failed to update staff member", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.error("Error resetting autocommit", e);
                }
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void delete(Integer staffId) {
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            conn.setAutoCommit(false);

            String getPersonIdSql = "SELECT person_id FROM staff WHERE staff_id = ?";
            PreparedStatement getStmt = conn.prepareStatement(getPersonIdSql);
            getStmt.setInt(1, staffId);
            ResultSet rs = getStmt.executeQuery();

            Integer personId = null;
            if (rs.next()) {
                personId = rs.getInt("person_id");
            }

            String deleteStaffSql = "DELETE FROM staff WHERE staff_id = ?";
            PreparedStatement deleteStaffStmt = conn.prepareStatement(deleteStaffSql);
            deleteStaffStmt.setInt(1, staffId);
            deleteStaffStmt.executeUpdate();

            if (personId != null) {
                String deletePersonSql = "DELETE FROM person WHERE person_id = ?";
                PreparedStatement deletePersonStmt = conn.prepareStatement(deletePersonSql);
                deletePersonStmt.setInt(1, personId);
                deletePersonStmt.executeUpdate();
            }

            conn.commit();
            LOGGER.debug("Staff member deleted with ID: {}", staffId);
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    LOGGER.error("Error rolling back transaction", rollbackEx);
                }
            }
            LOGGER.error("Error deleting staff member with ID: {}", staffId, e);
            throw new RuntimeException("Failed to delete staff member", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.error("Error resetting autocommit", e);
                }
                connectionPool.releaseConnection(conn);
            }
        }
    }

    private Staff mapResultSetToStaff(ResultSet rs) throws SQLException {
        ConcreteStaff staff = new ConcreteStaff();

        staff.setStaffId(rs.getInt("staff_id"));
        staff.setDepartmentId((Integer) rs.getObject("department_id"));
        staff.setTitle(rs.getString("title"));

        staff.setPersonId(rs.getInt("person_id"));
        staff.setFirstName(rs.getString("first_name"));
        staff.setLastName(rs.getString("last_name"));
        staff.setEmail(rs.getString("email"));

        return staff;
    }
}
