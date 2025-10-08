package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.EnrollmentDAO;
import com.solvd.university.dao.interfaces.ProgramDAO;
import com.solvd.university.dao.interfaces.StudentDAO;
import com.solvd.university.model.Enrollment;
import com.solvd.university.model.EnrollmentStatus;
import com.solvd.university.model.GradeLevel;
import com.solvd.university.model.Program;
import com.solvd.university.model.Student;
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

public class StudentDAOImpl implements StudentDAO {

    private static final Logger LOGGER = LogManager.getLogger(StudentDAOImpl.class);
    private final ConnectionPool connectionPool;
    private final EnrollmentDAO enrollmentDAO;

    public StudentDAOImpl(ProgramDAO programDAO, EnrollmentDAO enrollmentDAO) {
        this.connectionPool = ConnectionPool.getInstance();
        this.enrollmentDAO = enrollmentDAO;
    }

    @Override
    public void save(Student student) {
        String insertPersonSql = "INSERT INTO person (first_name, last_name, email) VALUES (?, ?, ?)";
        String insertStudentSql =
            "INSERT INTO student (person_id, age, student_number, " +
            "enrollment_status_id, grade_level_id, is_registered, balance) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updatePersonSql = "UPDATE person SET student_id = ?, university_id = ? WHERE person_id = ?";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            conn.setAutoCommit(false);

            try (
                PreparedStatement personStmt = conn.prepareStatement(insertPersonSql, Statement.RETURN_GENERATED_KEYS)
            ) {
                personStmt.setString(1, student.getFirstName());
                personStmt.setString(2, student.getLastName());
                personStmt.setString(3, student.getEmail());
                personStmt.executeUpdate();

                try (ResultSet rs = personStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int personId = rs.getInt(1);
                        student.setPersonId(personId);

                        try (
                            PreparedStatement studentStmt = conn.prepareStatement(
                                insertStudentSql,
                                Statement.RETURN_GENERATED_KEYS
                            )
                        ) {
                            studentStmt.setInt(1, personId);
                            studentStmt.setInt(2, student.getAge());
                            studentStmt.setInt(3, student.getStudentNumber());

                            studentStmt.setInt(4, student.getEnrollmentStatus().getEnrollmentStatusId());
                            studentStmt.setInt(5, student.getGradeLevel().getGradeLevelId());
                            studentStmt.setBoolean(6, student.isRegistered());
                            studentStmt.setDouble(7, student.getOutstandingBalance());
                            studentStmt.executeUpdate();

                            try (ResultSet studentRs = studentStmt.getGeneratedKeys()) {
                                if (studentRs.next()) {
                                    int studentId = studentRs.getInt(1);
                                    student.setStudentId(studentId);

                                    try (PreparedStatement updatePersonStmt = conn.prepareStatement(updatePersonSql)) {
                                        updatePersonStmt.setInt(1, studentId);

                                        Integer defaultUniversityId = 1;

                                        try (
                                            PreparedStatement checkStmt = conn.prepareStatement(
                                                "SELECT 1 FROM university WHERE university_id = ?"
                                            )
                                        ) {
                                            checkStmt.setInt(1, defaultUniversityId);
                                            try (ResultSet uniRs = checkStmt.executeQuery()) {
                                                if (!uniRs.next()) {
                                                    defaultUniversityId = null;
                                                    LOGGER.warn("Default university (ID=1) not found, setting to NULL");
                                                }
                                            }
                                        }

                                        if (defaultUniversityId != null) {
                                            updatePersonStmt.setInt(2, defaultUniversityId);
                                        } else {
                                            updatePersonStmt.setNull(2, java.sql.Types.INTEGER);
                                        }

                                        updatePersonStmt.setInt(3, personId);
                                        updatePersonStmt.executeUpdate();

                                        LOGGER.debug(
                                            "Updated person table with student_id: {} and university_id: {}",
                                            studentId,
                                            defaultUniversityId
                                        );
                                    }
                                }
                            }
                        }
                    }
                }
            }

            conn.commit();
            LOGGER.info(
                "Saved student: {} {} (ID: {})",
                student.getFirstName(),
                student.getLastName(),
                student.getStudentId()
            );
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    LOGGER.error("Error rolling back transaction", rollbackEx);
                }
            }
            LOGGER.error("Error saving student", e);
            throw new RuntimeException("Failed to save student", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.error("Error resetting auto-commit", e);
                }
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public Optional<Student> findById(String id) {
        String sql =
            "SELECT s.*, p.first_name, p.last_name, p.email, " +
            "es.display_name as enrollment_status_name, " +
            "gl.display_name as grade_level_name, gl.year as grade_year " +
            "FROM student s " +
            "JOIN person p ON s.person_id = p.person_id " +
            "JOIN enrollment_status es ON s.enrollment_status_id = es.enrollment_status_id " +
            "JOIN grade_level gl ON s.grade_level_id = gl.grade_level_id " +
            "WHERE s.student_number = ?";

        Connection conn = null;
        try {
            int studentNumber = Integer.parseInt(id.replace("STU-", ""));
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, studentNumber);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapResultSetToStudent(rs));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding student by ID: {}", id, e);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid student ID format: {}", id, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        String sql =
            "SELECT s.*, p.first_name, p.last_name, p.email, " +
            "es.display_name as enrollment_status_name, " +
            "gl.display_name as grade_level_name, gl.year as grade_year " +
            "FROM student s " +
            "JOIN person p ON s.person_id = p.person_id " +
            "JOIN enrollment_status es ON s.enrollment_status_id = es.enrollment_status_id " +
            "JOIN grade_level gl ON s.grade_level_id = gl.grade_level_id " +
            "WHERE p.email = ?";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapResultSetToStudent(rs));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding student by email: {}", email, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Student> findByEmailAndStudentNumber(String email, int studentNumber) {
        String sql =
            "SELECT s.*, p.first_name, p.last_name, p.email, " +
            "es.display_name as enrollment_status_name, " +
            "gl.display_name as grade_level_name, gl.year as grade_year " +
            "FROM student s " +
            "JOIN person p ON s.person_id = p.person_id " +
            "JOIN enrollment_status es ON s.enrollment_status_id = es.enrollment_status_id " +
            "JOIN grade_level gl ON s.grade_level_id = gl.grade_level_id " +
            "WHERE p.email = ? AND s.student_number = ?";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setInt(2, studentNumber);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapResultSetToStudent(rs));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding student by email and student number", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql =
            "SELECT s.*, p.first_name, p.last_name, p.email, " +
            "es.display_name as enrollment_status_name, " +
            "gl.display_name as grade_level_name, gl.year as grade_year " +
            "FROM student s " +
            "JOIN person p ON s.person_id = p.person_id " +
            "JOIN enrollment_status es ON s.enrollment_status_id = es.enrollment_status_id " +
            "JOIN grade_level gl ON s.grade_level_id = gl.grade_level_id " +
            "ORDER BY s.student_id";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapResultSetToStudent(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding all students", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return students;
    }

    @Override
    public void update(Student student) {
        String updatePersonSql =
            "UPDATE person SET first_name = ?, last_name = ?, email = ?, student_id = ?, university_id = ? WHERE person_id = ?";
        String updateStudentSql =
            "UPDATE student SET age = ?, enrollment_status_id = ?, " +
            "grade_level_id = ?, is_registered = ?, balance = ? WHERE student_id = ?";

        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement personStmt = conn.prepareStatement(updatePersonSql)) {
                personStmt.setString(1, student.getFirstName());
                personStmt.setString(2, student.getLastName());
                personStmt.setString(3, student.getEmail());
                personStmt.setInt(4, student.getStudentId());

                Integer universityId = null;
                if (student.getEnrolledProgram() != null && student.getEnrolledProgram().getUniversityId() != null) {
                    universityId = student.getEnrolledProgram().getUniversityId();

                    try (
                        PreparedStatement checkStmt = conn.prepareStatement(
                            "SELECT 1 FROM university WHERE university_id = ?"
                        )
                    ) {
                        checkStmt.setInt(1, universityId);
                        try (ResultSet rs = checkStmt.executeQuery()) {
                            if (!rs.next()) {
                                universityId = null;
                                LOGGER.warn(
                                    "University ID {} not found in database, setting person.university_id to NULL",
                                    universityId
                                );
                            }
                        }
                    }
                }

                if (universityId != null) {
                    personStmt.setInt(5, universityId);
                } else {
                    personStmt.setNull(5, java.sql.Types.INTEGER);
                }

                personStmt.setInt(6, student.getPersonId());
                personStmt.executeUpdate();
            }

            try (PreparedStatement studentStmt = conn.prepareStatement(updateStudentSql)) {
                studentStmt.setInt(1, student.getAge());
                studentStmt.setInt(2, student.getEnrollmentStatus().getEnrollmentStatusId());
                studentStmt.setInt(3, student.getGradeLevel().getGradeLevelId());
                studentStmt.setBoolean(4, student.isRegistered());
                studentStmt.setDouble(5, student.getOutstandingBalance());
                studentStmt.setInt(6, student.getStudentId());
                studentStmt.executeUpdate();
            }

            conn.commit();
            LOGGER.info(
                "Updated student: {} {} (ID: {})",
                student.getFirstName(),
                student.getLastName(),
                student.getStudentId()
            );
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    LOGGER.error("Error rolling back transaction", rollbackEx);
                }
            }
            LOGGER.error("Error updating student", e);
            throw new RuntimeException("Failed to update student", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.error("Error resetting auto-commit", e);
                }
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void delete(String id) {
        String deleteStudentSql = "DELETE FROM student WHERE student_number = ?";
        String deletePersonSql = "DELETE FROM person WHERE person_id = ?";

        Connection conn = null;
        try {
            int studentNumber = Integer.parseInt(id.replace("STU-", ""));
            conn = connectionPool.getConnection();
            conn.setAutoCommit(false);

            String getPersonIdSql = "SELECT person_id FROM student WHERE student_number = ?";
            Integer personId = null;

            try (PreparedStatement stmt = conn.prepareStatement(getPersonIdSql)) {
                stmt.setInt(1, studentNumber);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        personId = rs.getInt("person_id");
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(deleteStudentSql)) {
                stmt.setInt(1, studentNumber);
                stmt.executeUpdate();
            }

            if (personId != null) {
                try (PreparedStatement stmt = conn.prepareStatement(deletePersonSql)) {
                    stmt.setInt(1, personId);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            LOGGER.info("Deleted student with ID: {}", id);
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    LOGGER.error("Error rolling back transaction", rollbackEx);
                }
            }
            LOGGER.error("Error deleting student", e);
            throw new RuntimeException("Failed to delete student", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    LOGGER.error("Error resetting auto-commit", e);
                }
                connectionPool.releaseConnection(conn);
            }
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student(
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getInt("age"),
            rs.getString("email")
        );

        student.setStudentId(rs.getInt("student_id"));
        student.setPersonId(rs.getInt("person_id"));

        Integer enrollmentStatusId = rs.getInt("enrollment_status_id");
        String enrollmentStatusName = rs.getString("enrollment_status_name");

        if (enrollmentStatusId == 4 || "Enrolled".equals(enrollmentStatusName)) {
            student.setEnrollmentStatus(EnrollmentStatus.ENROLLED);
        } else if (enrollmentStatusId == 1 || "Applied".equals(enrollmentStatusName)) {
            student.setEnrollmentStatus(EnrollmentStatus.APPLIED);
        } else if (enrollmentStatusId == 8 || "Withdrawn".equals(enrollmentStatusName)) {
            student.setEnrollmentStatus(EnrollmentStatus.WITHDRAWN);
        } else {
            EnrollmentStatus status = new EnrollmentStatus();
            status.setEnrollmentStatusId(enrollmentStatusId);
            status.setDisplayName(enrollmentStatusName);
            student.setEnrollmentStatus(status);
        }

        int gradeYear = rs.getInt("grade_year");
        switch (gradeYear) {
            case 1:
                student.setGradeLevel(GradeLevel.FRESHMAN);
                break;
            case 2:
                student.setGradeLevel(GradeLevel.SOPHOMORE);
                break;
            case 3:
                student.setGradeLevel(GradeLevel.JUNIOR);
                break;
            case 4:
                student.setGradeLevel(GradeLevel.SENIOR);
                break;
            case 5:
                student.setGradeLevel(GradeLevel.GRADUATE);
                break;
            default:
                student.setGradeLevel(GradeLevel.FRESHMAN);
        }

        student.setRegistered(rs.getBoolean("is_registered"));

        double balance = rs.getDouble("balance");
        student.setOutstandingBalance(balance);

        List<Enrollment> enrollments = enrollmentDAO.findAll();
        Optional<Enrollment> activeEnrollment = enrollments
            .stream()
            .filter(e -> e.studentId() != null && e.studentId().equals(student.getStudentId()))
            .filter(e -> e.status() != null && e.status().equals(EnrollmentStatus.ENROLLED))
            .findFirst();

        if (activeEnrollment.isPresent()) {
            Integer programId = activeEnrollment.get().programId();
            if (programId != null) {
                Program enrolledProgram = activeEnrollment.get().program();
                if (enrolledProgram != null) {
                    student.setEnrolledProgram(enrolledProgram);
                    student.setEnrollmentStatus(activeEnrollment.get().status());
                } else {
                    LOGGER.warn(
                        "Student {} has enrollment but program not loaded in enrollment record",
                        student.getStudentNumber()
                    );
                }
            }
        }

        return student;
    }
}
