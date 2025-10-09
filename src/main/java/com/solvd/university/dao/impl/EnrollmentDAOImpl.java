package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.EnrollmentDAO;
import com.solvd.university.dao.interfaces.ProgramDAO;
import com.solvd.university.model.Enrollment;
import com.solvd.university.model.EnrollmentStatus;
import com.solvd.university.model.Program;
import com.solvd.university.model.Student;
import com.solvd.university.util.ConnectionPool;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnrollmentDAOImpl implements EnrollmentDAO {

    private static final Logger LOGGER = LogManager.getLogger(EnrollmentDAOImpl.class);
    private final ConnectionPool connectionPool;
    private final ProgramDAO programDAO;

    public EnrollmentDAOImpl(ProgramDAO programDAO) {
        this.connectionPool = ConnectionPool.getInstance();
        this.programDAO = programDAO;
    }

    @Override
    public void save(Enrollment enrollment) {
        String sql =
            "INSERT INTO enrollment (student_id, program_id, enrollment_date, enrollment_status_id) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, enrollment.studentId());
                stmt.setInt(2, enrollment.programId());
                stmt.setDate(3, Date.valueOf(enrollment.enrollmentDate()));
                stmt.setInt(4, enrollment.enrollmentStatusId());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int enrollmentId = rs.getInt(1);
                        LOGGER.info("Saved enrollment with ID: {}", enrollmentId);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error saving enrollment", e);
            throw new RuntimeException("Failed to save enrollment", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public Optional<Enrollment> findById(Integer enrollmentId) {
        String sql = "SELECT * FROM enrollment WHERE enrollment_id = ?";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, enrollmentId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapResultSetToEnrollment(rs));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding enrollment by ID: {}", enrollmentId, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Enrollment> findByStudent(Student student) {
        String sql = "SELECT * FROM enrollment WHERE student_id = ? ORDER BY enrollment_date DESC LIMIT 1";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, student.getStudentId());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapResultSetToEnrollment(rs));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding enrollment by student", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Enrollment> findAll() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollment ORDER BY enrollment_id";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapResultSetToEnrollment(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding all enrollments", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return enrollments;
    }

    @Override
    public void update(Enrollment enrollment) {
        String sql = "UPDATE enrollment SET enrollment_status_id = ? WHERE enrollment_id = ?";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, enrollment.enrollmentStatusId());
                stmt.setInt(2, enrollment.enrollmentId());
                stmt.executeUpdate();
            }

            LOGGER.info("Updated enrollment ID: {}", enrollment.enrollmentId());
        } catch (SQLException e) {
            LOGGER.error("Error updating enrollment", e);
            throw new RuntimeException("Failed to update enrollment", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void delete(Integer enrollmentId) {
        String sql = "DELETE FROM enrollment WHERE enrollment_id = ?";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, enrollmentId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    LOGGER.info("Deleted enrollment ID: {}", enrollmentId);
                } else {
                    LOGGER.warn("No enrollment found with ID: {}", enrollmentId);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error deleting enrollment: {}", enrollmentId, e);
            throw new RuntimeException("Failed to delete enrollment", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
        Integer enrollmentId = rs.getInt("enrollment_id");
        Integer studentId = rs.getInt("student_id");
        Integer programId = rs.getInt("program_id");
        Integer enrollmentStatusId = rs.getInt("enrollment_status_id");
        LocalDate enrollmentDate = rs.getDate("enrollment_date").toLocalDate();

        EnrollmentStatus status;
        if (enrollmentStatusId == 4) {
            status = EnrollmentStatus.ENROLLED;
        } else if (enrollmentStatusId == 1) {
            status = EnrollmentStatus.APPLIED;
        } else if (enrollmentStatusId == 8) {
            status = EnrollmentStatus.WITHDRAWN;
        } else {
            status = new EnrollmentStatus();
            status.setEnrollmentStatusId(enrollmentStatusId);
        }

        Program program = null;
        if (programId != null) {
            Optional<Program> programOpt = programDAO.findById(programId);
            if (programOpt.isPresent()) {
                program = programOpt.get();
            } else {
                LOGGER.warn("Enrollment {} references non-existent program_id {}", enrollmentId, programId);
            }
        }

        return new Enrollment(
            enrollmentId,
            studentId,
            programId,
            enrollmentStatusId,
            null,
            program,
            enrollmentDate,
            status
        );
    }
}
