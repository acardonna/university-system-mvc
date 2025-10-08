package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.DepartmentDAO;
import com.solvd.university.dao.interfaces.ProgramDAO;
import com.solvd.university.model.Department;
import com.solvd.university.model.Program;
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

public class ProgramDAOImpl implements ProgramDAO {

    private static final Logger LOGGER = LogManager.getLogger(ProgramDAOImpl.class);
    private final ConnectionPool connectionPool;
    private final DepartmentDAO departmentDAO;

    public ProgramDAOImpl(DepartmentDAO departmentDAO) {
        this.connectionPool = ConnectionPool.getInstance();
        this.departmentDAO = departmentDAO;
    }

    @Override
    public void save(Program program) {
        String sql =
            "INSERT INTO program (name, duration_years, price, department_id, university_id) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, program.getName());
                stmt.setInt(2, program.getDuration());
                stmt.setDouble(3, program.getRawPrice());
                stmt.setObject(4, program.getDepartmentId(), Types.INTEGER);
                stmt.setObject(5, program.getUniversityId(), Types.INTEGER);
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        program.setProgramId(rs.getInt(1));
                    }
                }
            }

            LOGGER.info("Saved program: {} (ID: {})", program.getName(), program.getProgramId());
        } catch (SQLException e) {
            LOGGER.error("Error saving program", e);
            throw new RuntimeException("Failed to save program", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public Optional<Program> findById(Integer programId) {
        String sql = "SELECT * FROM program WHERE program_id = ?";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, programId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapResultSetToProgram(rs));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding program by id: {}", programId, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Program> findByName(String name) {
        String sql = "SELECT * FROM program WHERE name = ?";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapResultSetToProgram(rs));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding program by name: {}", name, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Program> findByDepartment(Department<?> department) {
        List<Program> programs = new ArrayList<>();
        String sql = "SELECT * FROM program WHERE department_id = ?";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, department.getDepartmentId());

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        programs.add(mapResultSetToProgram(rs));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding programs by department", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return programs;
    }

    @Override
    public List<Program> findAll() {
        List<Program> programs = new ArrayList<>();
        String sql = "SELECT * FROM program ORDER BY program_id";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    programs.add(mapResultSetToProgram(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding all programs", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return programs;
    }

    @Override
    public void update(Program program) {
        String sql =
            "UPDATE program SET name = ?, duration_years = ?, price = ?, department_id = ?, university_id = ? WHERE program_id = ?";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, program.getName());
                stmt.setInt(2, program.getDuration());
                stmt.setDouble(3, program.getRawPrice());
                stmt.setObject(4, program.getDepartmentId(), Types.INTEGER);
                stmt.setObject(5, program.getUniversityId(), Types.INTEGER);
                stmt.setInt(6, program.getProgramId());
                stmt.executeUpdate();
            }

            LOGGER.info("Updated program: {} (ID: {})", program.getName(), program.getProgramId());
        } catch (SQLException e) {
            LOGGER.error("Error updating program", e);
            throw new RuntimeException("Failed to update program", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void delete(Integer programId) {
        String sql = "DELETE FROM program WHERE program_id = ?";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, programId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    LOGGER.info("Deleted program with ID: {}", programId);
                } else {
                    LOGGER.warn("No program found with ID: {}", programId);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error deleting program: {}", programId, e);
            throw new RuntimeException("Failed to delete program", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    private Program mapResultSetToProgram(ResultSet rs) throws SQLException {
        Program program = new Program();
        program.setProgramId(rs.getInt("program_id"));
        program.setName(rs.getString("name"));
        program.setDuration(rs.getInt("duration_years"));
        program.setPrice(rs.getDouble("price"));

        Integer departmentId = rs.getInt("department_id");
        program.setDepartmentId(departmentId);

        if (departmentId != null) {
            departmentDAO.findById(departmentId).ifPresent(program::setDepartment);
        }

        program.setUniversityId(rs.getInt("university_id"));

        return program;
    }
}
