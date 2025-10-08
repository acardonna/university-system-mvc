package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.CourseGradeDAO;
import com.solvd.university.model.CourseGrade;
import com.solvd.university.util.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CourseGradeDAOImpl implements CourseGradeDAO {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ConnectionPool connectionPool;

    public CourseGradeDAOImpl() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public void save(CourseGrade grade) {
        String sql =
            "INSERT INTO course_grade (course_id, subject, value, semester, recorded_at) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, grade.getCourseId());
            stmt.setString(2, grade.getSubject());
            stmt.setDouble(3, grade.getValue());
            stmt.setInt(4, grade.getSemester());
            stmt.setTimestamp(5, Timestamp.valueOf(grade.getRecordedAt()));

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    grade.setCourseGradeId(rs.getInt(1));
                    LOGGER.info("Course grade saved successfully with ID: {}", grade.getCourseGradeId());
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error saving course grade: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save course grade", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public CourseGrade findById(Integer id) {
        String sql = "SELECT * FROM course_grade WHERE course_grade_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCourseGrade(rs);
            }

            return null;
        } catch (SQLException e) {
            LOGGER.error("Error finding course grade by ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to find course grade", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public List<CourseGrade> findAll() {
        String sql = "SELECT * FROM course_grade ORDER BY course_id, semester, recorded_at";
        Connection conn = null;
        List<CourseGrade> grades = new ArrayList<>();

        try {
            conn = connectionPool.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                grades.add(mapResultSetToCourseGrade(rs));
            }

            return grades;
        } catch (SQLException e) {
            LOGGER.error("Error finding all course grades: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to find course grades", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public List<CourseGrade> findByCourseId(Integer courseId) {
        String sql = "SELECT * FROM course_grade WHERE course_id = ? ORDER BY semester, recorded_at";
        Connection conn = null;
        List<CourseGrade> grades = new ArrayList<>();

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                grades.add(mapResultSetToCourseGrade(rs));
            }

            LOGGER.info("Found {} grades for course ID {}", grades.size(), courseId);
            return grades;
        } catch (SQLException e) {
            LOGGER.error("Error finding grades for course {}: {}", courseId, e.getMessage(), e);
            throw new RuntimeException("Failed to find course grades", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public List<CourseGrade> findByCourseIdAndSemester(Integer courseId, Integer semester) {
        String sql = "SELECT * FROM course_grade WHERE course_id = ? AND semester = ? ORDER BY recorded_at";
        Connection conn = null;
        List<CourseGrade> grades = new ArrayList<>();

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
            stmt.setInt(2, semester);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                grades.add(mapResultSetToCourseGrade(rs));
            }

            LOGGER.info("Found {} grades for course ID {} in semester {}", grades.size(), courseId, semester);
            return grades;
        } catch (SQLException e) {
            LOGGER.error("Error finding grades for course {} semester {}: {}", courseId, semester, e.getMessage(), e);
            throw new RuntimeException("Failed to find course grades", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void update(CourseGrade grade) {
        String sql =
            "UPDATE course_grade SET course_id = ?, subject = ?, value = ?, semester = ?, recorded_at = ? " +
            "WHERE course_grade_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, grade.getCourseId());
            stmt.setString(2, grade.getSubject());
            stmt.setDouble(3, grade.getValue());
            stmt.setInt(4, grade.getSemester());
            stmt.setTimestamp(5, Timestamp.valueOf(grade.getRecordedAt()));
            stmt.setInt(6, grade.getCourseGradeId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("Course grade updated successfully: ID {}", grade.getCourseGradeId());
            } else {
                LOGGER.warn("No course grade found with ID {} to update", grade.getCourseGradeId());
            }
        } catch (SQLException e) {
            LOGGER.error("Error updating course grade: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update course grade", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM course_grade WHERE course_grade_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("Course grade deleted successfully: ID {}", id);
            } else {
                LOGGER.warn("No course grade found with ID {} to delete", id);
            }
        } catch (SQLException e) {
            LOGGER.error("Error deleting course grade: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete course grade", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    private CourseGrade mapResultSetToCourseGrade(ResultSet rs) throws SQLException {
        CourseGrade grade = new CourseGrade();
        grade.setCourseGradeId(rs.getInt("course_grade_id"));
        grade.setCourseId(rs.getInt("course_id"));
        grade.setSubject(rs.getString("subject"));
        grade.setValue(rs.getDouble("value"));
        grade.setSemester(rs.getInt("semester"));

        Timestamp timestamp = rs.getTimestamp("recorded_at");
        if (timestamp != null) {
            grade.setRecordedAt(timestamp.toLocalDateTime());
        }

        return grade;
    }
}
