package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.DepartmentDAO;
import com.solvd.university.dao.interfaces.ProfessorDAO;
import com.solvd.university.model.Department;
import com.solvd.university.model.Professor;
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

public class ProfessorDAOImpl implements ProfessorDAO {

    private static final Logger LOGGER = LogManager.getLogger(ProfessorDAOImpl.class);
    private final ConnectionPool connectionPool;
    private final DepartmentDAO departmentDAO;

    public ProfessorDAOImpl(DepartmentDAO departmentDAO) {
        this.connectionPool = ConnectionPool.getInstance();
        this.departmentDAO = departmentDAO;
    }

    @Override
    public void save(Professor professor) {
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            conn.setAutoCommit(false);

            String personSql = "INSERT INTO person (first_name, last_name, email) VALUES (?, ?, ?)";
            PreparedStatement personStmt = conn.prepareStatement(personSql, Statement.RETURN_GENERATED_KEYS);
            personStmt.setString(1, professor.getFirstName());
            personStmt.setString(2, professor.getLastName());
            personStmt.setString(3, professor.getEmail());
            personStmt.executeUpdate();

            ResultSet rsPerson = personStmt.getGeneratedKeys();
            if (rsPerson.next()) {
                professor.setPersonId(rsPerson.getInt(1));
            }

            String staffSql = "INSERT INTO staff (person_id, department_id, title) VALUES (?, ?, ?)";
            PreparedStatement staffStmt = conn.prepareStatement(staffSql, Statement.RETURN_GENERATED_KEYS);
            staffStmt.setInt(1, professor.getPersonId());
            staffStmt.setObject(
                2,
                professor.getDepartment() != null ? professor.getDepartment().getDepartmentId() : null
            );
            staffStmt.setString(3, professor.getTitle());
            staffStmt.executeUpdate();

            ResultSet rsStaff = staffStmt.getGeneratedKeys();
            if (rsStaff.next()) {
                professor.setStaffId(rsStaff.getInt(1));
            }

            String profSql = "INSERT INTO professor (person_id, department_id) VALUES (?, ?)";
            PreparedStatement profStmt = conn.prepareStatement(profSql, Statement.RETURN_GENERATED_KEYS);
            profStmt.setInt(1, professor.getPersonId());
            profStmt.setObject(
                2,
                professor.getDepartment() != null ? professor.getDepartment().getDepartmentId() : null
            );
            profStmt.executeUpdate();

            ResultSet rsProf = profStmt.getGeneratedKeys();
            if (rsProf.next()) {
                professor.setProfessorId(rsProf.getInt(1));
            }

            String updatePersonSql =
                "UPDATE person SET staff_id = ?, professor_id = ?, university_id = ? WHERE person_id = ?";
            PreparedStatement updatePersonStmt = conn.prepareStatement(updatePersonSql);
            updatePersonStmt.setInt(1, professor.getStaffId());
            updatePersonStmt.setInt(2, professor.getProfessorId());

            Integer universityId = null;
            if (professor.getDepartment() != null && professor.getDepartment().getUniversityId() != null) {
                universityId = professor.getDepartment().getUniversityId();
            }

            if (universityId != null) {
                updatePersonStmt.setInt(3, universityId);
            } else {
                updatePersonStmt.setNull(3, java.sql.Types.INTEGER);
            }

            updatePersonStmt.setInt(4, professor.getPersonId());
            updatePersonStmt.executeUpdate();

            LOGGER.debug(
                "Updated person table with staff_id: {}, professor_id: {}, and university_id: {}",
                professor.getStaffId(),
                professor.getProfessorId(),
                universityId
            );

            conn.commit();
            LOGGER.info(
                "Professor saved successfully: ID {}, Name: {}",
                professor.getProfessorId(),
                professor.getFullName()
            );
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    LOGGER.error("Transaction rolled back due to error");
                } catch (SQLException ex) {
                    LOGGER.error("Error rolling back transaction", ex);
                }
            }
            LOGGER.error("Error saving professor: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save professor", e);
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
    public Optional<Professor> findById(Integer professorId) {
        String sql = """
                SELECT prof.professor_id, prof.department_id, p.person_id, p.first_name, p.last_name, p.email,
                       s.staff_id, s.title
                FROM professor prof
                JOIN person p ON prof.person_id = p.person_id
                JOIN staff s ON prof.person_id = s.person_id
                WHERE prof.professor_id = ?
            """;

        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, professorId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToProfessor(rs));
            }

            return Optional.empty();
        } catch (SQLException e) {
            LOGGER.error("Error finding professor by id {}: {}", professorId, e.getMessage(), e);
            throw new RuntimeException("Failed to find professor", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public Optional<Professor> findByFullName(String firstName, String lastName) {
        String sql = """
                SELECT prof.professor_id, prof.department_id, p.person_id, p.first_name, p.last_name, p.email,
                       s.staff_id, s.title
                FROM professor prof
                JOIN person p ON prof.person_id = p.person_id
                JOIN staff s ON prof.person_id = s.person_id
                WHERE p.first_name = ? AND p.last_name = ?
            """;

        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToProfessor(rs));
            }

            return Optional.empty();
        } catch (SQLException e) {
            LOGGER.error("Error finding professor by name {} {}: {}", firstName, lastName, e.getMessage(), e);
            throw new RuntimeException("Failed to find professor", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public List<Professor> findAll() {
        String sql = """
                SELECT prof.professor_id, prof.department_id, p.person_id, p.first_name, p.last_name, p.email,
                       s.staff_id, s.title
                FROM professor prof
                JOIN person p ON prof.person_id = p.person_id
                JOIN staff s ON prof.person_id = s.person_id
                ORDER BY p.last_name, p.first_name
            """;

        Connection conn = null;
        List<Professor> professors = new ArrayList<>();

        try {
            conn = connectionPool.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                professors.add(mapResultSetToProfessor(rs));
            }

            return professors;
        } catch (SQLException e) {
            LOGGER.error("Error finding all professors: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to find professors", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void update(Professor professor) {
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            conn.setAutoCommit(false);

            String personSql = "UPDATE person SET first_name = ?, last_name = ?, email = ? WHERE person_id = ?";
            PreparedStatement personStmt = conn.prepareStatement(personSql);
            personStmt.setString(1, professor.getFirstName());
            personStmt.setString(2, professor.getLastName());
            personStmt.setString(3, professor.getEmail());
            personStmt.setInt(4, professor.getPersonId());
            personStmt.executeUpdate();

            String staffSql = "UPDATE staff SET department_id = ?, title = ? WHERE staff_id = ?";
            PreparedStatement staffStmt = conn.prepareStatement(staffSql);
            staffStmt.setObject(
                1,
                professor.getDepartment() != null ? professor.getDepartment().getDepartmentId() : null
            );
            staffStmt.setString(2, professor.getTitle());
            staffStmt.setInt(3, professor.getStaffId());
            staffStmt.executeUpdate();

            String profSql = "UPDATE professor SET department_id = ? WHERE professor_id = ?";
            PreparedStatement profStmt = conn.prepareStatement(profSql);
            profStmt.setObject(
                1,
                professor.getDepartment() != null ? professor.getDepartment().getDepartmentId() : null
            );
            profStmt.setInt(2, professor.getProfessorId());
            profStmt.executeUpdate();

            conn.commit();
            LOGGER.info("Professor updated successfully: ID {}", professor.getProfessorId());
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.error("Error rolling back transaction", ex);
                }
            }
            LOGGER.error("Error updating professor: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update professor", e);
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
    public void delete(Integer professorId) {
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            conn.setAutoCommit(false);

            String selectSql =
                "SELECT staff_id, person_id FROM professor p " +
                "JOIN staff s ON p.staff_id = s.staff_id WHERE p.professor_id = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setInt(1, professorId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int staffId = rs.getInt("staff_id");
                int personId = rs.getInt("person_id");

                String profSql = "DELETE FROM professor WHERE professor_id = ?";
                PreparedStatement profStmt = conn.prepareStatement(profSql);
                profStmt.setInt(1, professorId);
                profStmt.executeUpdate();

                String staffSql = "DELETE FROM staff WHERE staff_id = ?";
                PreparedStatement staffStmt = conn.prepareStatement(staffSql);
                staffStmt.setInt(1, staffId);
                staffStmt.executeUpdate();

                String personSql = "DELETE FROM person WHERE person_id = ?";
                PreparedStatement personStmt = conn.prepareStatement(personSql);
                personStmt.setInt(1, personId);
                personStmt.executeUpdate();

                conn.commit();
                LOGGER.info("Professor deleted successfully: ID {}", professorId);
            } else {
                LOGGER.warn("No professor found with ID: {}", professorId);
                conn.rollback();
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.error("Error rolling back transaction", ex);
                }
            }
            LOGGER.error("Error deleting professor: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete professor", e);
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

    private Professor mapResultSetToProfessor(ResultSet rs) throws SQLException {
        Professor professor = new Professor();
        professor.setProfessorId(rs.getInt("professor_id"));
        professor.setPersonId(rs.getInt("person_id"));
        professor.setFirstName(rs.getString("first_name"));
        professor.setLastName(rs.getString("last_name"));
        professor.setEmail(rs.getString("email"));
        professor.setStaffId(rs.getInt("staff_id"));
        professor.setTitle(rs.getString("title"));

        Integer deptId = rs.getObject("department_id", Integer.class);
        if (deptId != null) {
            Optional<Department<?>> department = departmentDAO.findById(deptId);
            department.ifPresent(professor::setDepartment);
            if (department.isEmpty()) {
                LOGGER.warn("Professor {} references non-existent department_id {}", professor.getFullName(), deptId);
            }
        }

        return professor;
    }
}
