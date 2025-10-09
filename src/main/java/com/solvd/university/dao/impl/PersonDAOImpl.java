package com.solvd.university.dao.impl;

import com.solvd.university.dao.interfaces.PersonDAO;
import com.solvd.university.model.ConcretePerson;
import com.solvd.university.model.Person;
import com.solvd.university.util.ConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersonDAOImpl implements PersonDAO {

    private static final Logger LOGGER = LogManager.getLogger(PersonDAOImpl.class);
    private final ConnectionPool connectionPool;

    public PersonDAOImpl() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public void save(Person person) {
        String sql =
            "INSERT INTO person (first_name, last_name, email, university_id, professor_id, student_id, staff_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, person.getFirstName());
            stmt.setString(2, person.getLastName());
            stmt.setString(3, person.getEmail());
            stmt.setObject(4, null);
            stmt.setObject(5, null);
            stmt.setObject(6, null);
            stmt.setObject(7, null);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                person.setPersonId(rs.getInt(1));
                LOGGER.debug("Person saved with ID: {}", person.getPersonId());
            }
        } catch (SQLException e) {
            LOGGER.error("Error saving person: {}", person.getFullName(), e);
            throw new RuntimeException("Failed to save person", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public Optional<Person> findById(Integer personId) {
        String sql = "SELECT * FROM person WHERE person_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, personId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToPerson(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding person by ID: {}", personId, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Person> findByEmail(String email) {
        String sql = "SELECT * FROM person WHERE email = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToPerson(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding person by email: {}", email, e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Person> findAll() {
        String sql = "SELECT * FROM person";
        List<Person> persons = new ArrayList<>();
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                persons.add(mapResultSetToPerson(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Error retrieving all persons", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }

        return persons;
    }

    @Override
    public void update(Person person) {
        String sql = "UPDATE person SET first_name = ?, last_name = ?, email = ? WHERE person_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, person.getFirstName());
            stmt.setString(2, person.getLastName());
            stmt.setString(3, person.getEmail());
            stmt.setInt(4, person.getPersonId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.debug("Person updated: {}", person.getFullName());
            }
        } catch (SQLException e) {
            LOGGER.error("Error updating person: {}", person.getFullName(), e);
            throw new RuntimeException("Failed to update person", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    @Override
    public void delete(Integer personId) {
        String sql = "DELETE FROM person WHERE person_id = ?";
        Connection conn = null;

        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, personId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.debug("Person deleted with ID: {}", personId);
            }
        } catch (SQLException e) {
            LOGGER.error("Error deleting person with ID: {}", personId, e);
            throw new RuntimeException("Failed to delete person", e);
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    private Person mapResultSetToPerson(ResultSet rs) throws SQLException {
        ConcretePerson person = new ConcretePerson();
        person.setPersonId(rs.getInt("person_id"));
        person.setFirstName(rs.getString("first_name"));
        person.setLastName(rs.getString("last_name"));
        person.setEmail(rs.getString("email"));
        return person;
    }
}
