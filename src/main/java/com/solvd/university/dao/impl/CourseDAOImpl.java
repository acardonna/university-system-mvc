package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.ClassroomDAO;
import com.solvd.university.dao.interfaces.CourseDAO;
import com.solvd.university.dao.interfaces.DepartmentDAO;
import com.solvd.university.dao.interfaces.ProfessorDAO;
import com.solvd.university.model.Classroom;
import com.solvd.university.model.Course;
import com.solvd.university.model.CourseDifficulty;
import com.solvd.university.model.Department;
import com.solvd.university.model.Professor;
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

public class CourseDAOImpl implements CourseDAO {

    private static final Logger LOGGER = LogManager.getLogger(CourseDAOImpl.class);
    private final ConnectionPool connectionPool;
    private final DepartmentDAO departmentDAO;
    private final ProfessorDAO professorDAO;
    private final ClassroomDAO classroomDAO;

    public CourseDAOImpl(DepartmentDAO departmentDAO, ProfessorDAO professorDAO, ClassroomDAO classroomDAO) {
        this.connectionPool = ConnectionPool.getInstance();
        this.departmentDAO = departmentDAO;
        this.professorDAO = professorDAO;
        this.classroomDAO = classroomDAO;
    }

    @Override
    public void save(Course<?, ?> course) {
        String sql =
            "INSERT INTO course (course_code, course_name, credit_hours, professor_id, " +
            "department_id, start_at, end_at, classroom_id, course_difficulty_id, university_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getCreditHours());

            if (course.getProfessorId() != null) {
                stmt.setInt(4, course.getProfessorId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            if (course.getDepartmentId() != null) {
                stmt.setInt(5, course.getDepartmentId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            if (course.getScheduledStart() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(course.getScheduledStart()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }

            if (course.getScheduledEnd() != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(course.getScheduledEnd()));
            } else {
                stmt.setNull(7, Types.TIMESTAMP);
            }

            if (course.getClassroomId() != null) {
                stmt.setInt(8, course.getClassroomId());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            if (course.getCourseDifficultyId() != null) {
                stmt.setInt(9, course.getCourseDifficultyId());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }

            if (course.getUniversityId() != null) {
                stmt.setInt(10, course.getUniversityId());
            } else {
                stmt.setNull(10, Types.INTEGER);
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    course.setCourseId(generatedKeys.getInt(1));
                    LOGGER.info("Course saved successfully with ID: {}", course.getCourseId());
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to save course: {}", course.getCourseCode(), e);
            throw new RuntimeException("Failed to save course", e);
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
    public Optional<Course<?, ?>> findById(String courseCode) {
        String sql =
            "SELECT c.*, cd.display_name AS difficulty_name, cd.lvl AS difficulty_level " +
            "FROM course c " +
            "LEFT JOIN course_difficulty cd ON c.course_difficulty_id = cd.course_difficulty_id " +
            "WHERE c.course_code = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Course<?, ?> course = mapResultSetToCourse(rs);
                return Optional.of(course);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to find course by code: {}", courseCode, e);
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
    public List<Course<?, ?>> findAll() {
        String sql =
            "SELECT c.*, cd.display_name AS difficulty_name, cd.lvl AS difficulty_level " +
            "FROM course c " +
            "LEFT JOIN course_difficulty cd ON c.course_difficulty_id = cd.course_difficulty_id";

        List<Course<?, ?>> courses = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Course<?, ?> course = mapResultSetToCourse(rs);
                courses.add(course);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to retrieve all courses", e);
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
        return courses;
    }

    @Override
    public void update(Course<?, ?> course) {
        String sql =
            "UPDATE course SET course_name = ?, credit_hours = ?, professor_id = ?, " +
            "department_id = ?, start_at = ?, end_at = ?, classroom_id = ?, " +
            "course_difficulty_id = ?, university_id = ? WHERE course_code = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, course.getCourseName());
            stmt.setInt(2, course.getCreditHours());

            if (course.getProfessorId() != null) {
                stmt.setInt(3, course.getProfessorId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            if (course.getDepartmentId() != null) {
                stmt.setInt(4, course.getDepartmentId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            if (course.getScheduledStart() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(course.getScheduledStart()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }

            if (course.getScheduledEnd() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(course.getScheduledEnd()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }

            if (course.getClassroomId() != null) {
                stmt.setInt(7, course.getClassroomId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            if (course.getCourseDifficultyId() != null) {
                stmt.setInt(8, course.getCourseDifficultyId());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            if (course.getUniversityId() != null) {
                stmt.setInt(9, course.getUniversityId());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }

            stmt.setString(10, course.getCourseCode());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Course updated successfully: {}", course.getCourseCode());
            } else {
                LOGGER.warn("No course found with code: {}", course.getCourseCode());
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to update course: {}", course.getCourseCode(), e);
            throw new RuntimeException("Failed to update course", e);
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
    public void delete(String courseCode) {
        String sql = "DELETE FROM course WHERE course_code = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = connectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseCode);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Course deleted successfully: {}", courseCode);
            } else {
                LOGGER.warn("No course found with code: {}", courseCode);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to delete course: {}", courseCode, e);
            throw new RuntimeException("Failed to delete course", e);
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

    private Course<?, ?> mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course<Object, Department<Object>> course = new Course<>();

        course.setCourseId(rs.getInt("course_id"));
        course.setCourseCode(rs.getString("course_code"));
        course.setCourseName(rs.getString("course_name"));
        course.setCreditHours(rs.getInt("credit_hours"));

        Integer professorId = rs.getObject("professor_id", Integer.class);
        course.setProfessorId(professorId);

        Integer departmentId = rs.getObject("department_id", Integer.class);
        course.setDepartmentId(departmentId);

        Integer classroomId = rs.getObject("classroom_id", Integer.class);
        course.setClassroomId(classroomId);

        Integer courseDifficultyId = rs.getObject("course_difficulty_id", Integer.class);
        course.setCourseDifficultyId(courseDifficultyId);

        Integer universityId = rs.getObject("university_id", Integer.class);
        course.setUniversityId(universityId);

        Timestamp startTimestamp = rs.getTimestamp("start_at");
        if (startTimestamp != null) {
            course.setScheduledStart(startTimestamp.toLocalDateTime());
        }

        Timestamp endTimestamp = rs.getTimestamp("end_at");
        if (endTimestamp != null) {
            course.setScheduledEnd(endTimestamp.toLocalDateTime());
        }

        String difficultyName = rs.getString("difficulty_name");
        if (difficultyName != null && courseDifficultyId != null) {
            int difficultyLevel = rs.getInt("difficulty_level");
            CourseDifficulty difficulty = new CourseDifficulty(courseDifficultyId, difficultyName, difficultyLevel);
            course.setDifficulty(difficulty);
        }

        if (departmentId != null) {
            Optional<Department<?>> department = departmentDAO.findById(departmentId);
            if (department.isPresent()) {
                @SuppressWarnings("unchecked")
                Department<Object> dept = (Department<Object>) department.get();
                course.setDepartment(dept);
            } else {
                LOGGER.warn("Course {} references non-existent department_id {}", course.getCourseCode(), departmentId);
            }
        }

        if (professorId != null) {
            Optional<Professor> professor = professorDAO.findById(professorId);
            professor.ifPresent(course::setProfessor);
            if (professor.isEmpty()) {
                LOGGER.warn("Course {} references non-existent professor_id {}", course.getCourseCode(), professorId);
            }
        }

        if (classroomId != null) {
            Optional<Classroom> classroom = classroomDAO.findById(classroomId);
            classroom.ifPresent(course::setClassroom);
            if (classroom.isEmpty()) {
                LOGGER.warn("Course {} references non-existent classroom_id {}", course.getCourseCode(), classroomId);
            }
        }

        return course;
    }
}
