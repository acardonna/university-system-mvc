package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.DepartmentDAO;
import com.solvd.university.model.ArtsDepartment;
import com.solvd.university.model.BusinessDepartment;
import com.solvd.university.model.ComputerScienceDepartment;
import com.solvd.university.model.Department;
import com.solvd.university.model.EngineeringDepartment;
import com.solvd.university.model.MathematicsDepartment;
import com.solvd.university.util.ConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DepartmentDAOImpl implements DepartmentDAO {

    private static final Logger LOGGER = LogManager.getLogger(DepartmentDAOImpl.class);
    private final ConnectionPool connectionPool;

    public DepartmentDAOImpl() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public void save(Department<?> department) {
        String sql = "INSERT INTO department (name, code, university_id) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, department.getName());
            stmt.setString(2, department.getDepartmentCode().toString());

            if (department.getUniversityId() != null) {
                stmt.setInt(3, department.getUniversityId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    department.setDepartmentId(generatedKeys.getInt(1));
                    LOGGER.info("Department saved successfully with ID: {}", department.getDepartmentId());
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to save department: {}", department.getName(), e);
            throw new RuntimeException("Failed to save department", e);
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
    public Optional<Department<?>> findById(Integer departmentId) {
        String sql = "SELECT * FROM department WHERE department_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, departmentId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Department<?> department = mapResultSetToDepartment(rs);
                return Optional.of(department);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to find department by ID: {}", departmentId, e);
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
    public Optional<Department<?>> findByCode(String code) {
        String sql = "SELECT * FROM department WHERE code = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, code);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Department<?> department = mapResultSetToDepartment(rs);
                return Optional.of(department);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to find department by code: {}", code, e);
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
    public List<Department<?>> findAll() {
        String sql = "SELECT * FROM department";

        List<Department<?>> departments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Department<?> department = mapResultSetToDepartment(rs);
                departments.add(department);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to retrieve all departments", e);
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
        return departments;
    }

    @Override
    public List<Department<?>> findByUniversity(Integer universityId) {
        String sql = "SELECT * FROM department WHERE university_id = ?";

        List<Department<?>> departments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, universityId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Department<?> department = mapResultSetToDepartment(rs);
                departments.add(department);
            }
            LOGGER.info("Found {} departments for university ID: {}", departments.size(), universityId);
        } catch (SQLException e) {
            LOGGER.error("Failed to retrieve departments for university ID: {}", universityId, e);
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
        return departments;
    }

    @Override
    public void update(Department<?> department) {
        String sql = "UPDATE department SET name = ?, code = ?, university_id = ? WHERE department_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, department.getName());
            stmt.setString(2, department.getDepartmentCode().toString());

            if (department.getUniversityId() != null) {
                stmt.setInt(3, department.getUniversityId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setInt(4, department.getDepartmentId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Department updated successfully: {}", department.getName());
            } else {
                LOGGER.warn("No department found with ID: {}", department.getDepartmentId());
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to update department: {}", department.getName(), e);
            throw new RuntimeException("Failed to update department", e);
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
    public void delete(Integer departmentId) {
        String sql = "DELETE FROM department WHERE department_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, departmentId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Department deleted successfully with ID: {}", departmentId);
            } else {
                LOGGER.warn("No department found with ID: {}", departmentId);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to delete department with ID: {}", departmentId, e);
            throw new RuntimeException("Failed to delete department", e);
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

    private Department<?> mapResultSetToDepartment(ResultSet rs) throws SQLException {
        Integer departmentId = rs.getInt("department_id");
        String name = rs.getString("name");
        String code = rs.getString("code");
        Integer universityId = rs.getObject("university_id", Integer.class);

        Department<String> department;
        switch (code.toUpperCase()) {
            case "CS":
                department = new ComputerScienceDepartment();
                break;
            case "MATH":
                department = new MathematicsDepartment();
                break;
            case "ENGR":
            case "ENG":
                department = new EngineeringDepartment();
                break;
            case "BUS":
            case "BUSINESS":
                department = new BusinessDepartment();
                break;
            case "ARTS":
            case "ART":
                department = new ArtsDepartment();
                break;
            default:
                LOGGER.warn("Unknown department code: {}. Using ComputerScienceDepartment as fallback.", code);
                department = new ComputerScienceDepartment();
        }

        department.setDepartmentId(departmentId);
        department.setName(name);
        department.setUniversityId(universityId);

        return department;
    }
}
