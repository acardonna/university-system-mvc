package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.StudentGradeDAO;
import com.solvd.university.model.StudentGrade;
import com.solvd.university.util.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StudentGradeDAOImpl implements StudentGradeDAO {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ConnectionPool connectionPool;

    public StudentGradeDAOImpl() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public void save(StudentGrade grade) {
        String sql = "INSERT INTO student_grade (student_id, subject, value, semester) VALUES (?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, grade.getStudentId());
            stmt.setString(2, grade.getSubject());
            stmt.setDouble(3, grade.getValue());
            stmt.setInt(4, grade.getSemester());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    grade.setStudentGradeId(rs.getInt(1));
                    LOGGER.info("Student grade saved successfully with ID: {}", grade.getStudentGradeId());
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error saving student grade: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save student grade", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public StudentGrade findById(Integer id) {
        String sql = "SELECT * FROM student_grade WHERE student_grade_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToStudentGrade(rs);
            }

            return null;
        } catch (SQLException e) {
            LOGGER.error("Error finding student grade by ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to find student grade", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public List<StudentGrade> findAll() {
        String sql = "SELECT * FROM student_grade ORDER BY student_id, semester, subject";
        Connection conn = null;
        List<StudentGrade> grades = new ArrayList<>();

        try {
            conn = connectionPool.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                grades.add(mapResultSetToStudentGrade(rs));
            }

            return grades;
        } catch (SQLException e) {
            LOGGER.error("Error finding all student grades: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to find student grades", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public List<StudentGrade> findByStudentId(Integer studentId) {
        String sql = "SELECT * FROM student_grade WHERE student_id = ? ORDER BY semester, subject";
        Connection conn = null;
        List<StudentGrade> grades = new ArrayList<>();

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                grades.add(mapResultSetToStudentGrade(rs));
            }

            LOGGER.info("Found {} grades for student ID {}", grades.size(), studentId);
            return grades;
        } catch (SQLException e) {
            LOGGER.error("Error finding grades for student {}: {}", studentId, e.getMessage(), e);
            throw new RuntimeException("Failed to find student grades", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public List<StudentGrade> findByStudentIdAndSemester(Integer studentId, Integer semester) {
        String sql = "SELECT * FROM student_grade WHERE student_id = ? AND semester = ? ORDER BY subject";
        Connection conn = null;
        List<StudentGrade> grades = new ArrayList<>();

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            stmt.setInt(2, semester);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                grades.add(mapResultSetToStudentGrade(rs));
            }

            LOGGER.info("Found {} grades for student ID {} in semester {}", grades.size(), studentId, semester);
            return grades;
        } catch (SQLException e) {
            LOGGER.error("Error finding grades for student {} semester {}: {}", studentId, semester, e.getMessage(), e);
            throw new RuntimeException("Failed to find student grades", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void update(StudentGrade grade) {
        String sql =
            "UPDATE student_grade SET student_id = ?, subject = ?, value = ?, semester = ? " +
            "WHERE student_grade_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, grade.getStudentId());
            stmt.setString(2, grade.getSubject());
            stmt.setDouble(3, grade.getValue());
            stmt.setInt(4, grade.getSemester());
            stmt.setInt(5, grade.getStudentGradeId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("Student grade updated successfully: ID {}", grade.getStudentGradeId());
            } else {
                LOGGER.warn("No student grade found with ID {} to update", grade.getStudentGradeId());
            }
        } catch (SQLException e) {
            LOGGER.error("Error updating student grade: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update student grade", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM student_grade WHERE student_grade_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("Student grade deleted successfully: ID {}", id);
            } else {
                LOGGER.warn("No student grade found with ID {} to delete", id);
            }
        } catch (SQLException e) {
            LOGGER.error("Error deleting student grade: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete student grade", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    private StudentGrade mapResultSetToStudentGrade(ResultSet rs) throws SQLException {
        StudentGrade grade = new StudentGrade();
        grade.setStudentGradeId(rs.getInt("student_grade_id"));
        grade.setStudentId(rs.getInt("student_id"));
        grade.setSubject(rs.getString("subject"));
        grade.setValue(rs.getDouble("value"));
        grade.setSemester(rs.getInt("semester"));
        return grade;
    }
}
