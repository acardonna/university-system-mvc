package com.solvd.university.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.dao.factory.DAOFactory;
import com.solvd.university.dao.interfaces.PersonDAO;
import com.solvd.university.model.ConcretePerson;
import com.solvd.university.model.Person;
import com.solvd.university.service.interfaces.PersonService;

public class PersonServiceImpl implements PersonService {

    private static final Logger LOGGER = LogManager.getLogger(PersonServiceImpl.class);
    private final PersonDAO personDAO;

    public PersonServiceImpl() {
        this.personDAO = DAOFactory.create(PersonDAO.class);
    }

    @Override
    public Person addPerson(String firstName, String lastName, String email) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (personDAO.findByEmail(email).isPresent()) {
            LOGGER.warn("Person with email '{}' already exists", email);
            return personDAO.findByEmail(email).orElseThrow();
        }

        Person person = new ConcretePerson(firstName, lastName, email);
        personDAO.save(person);
        LOGGER.info("Person '{}' added successfully", person.getFullName());
        return person;
    }

    @Override
    public Person getPersonById(Integer personId) {
        if (personId == null) {
            throw new IllegalArgumentException("Person ID cannot be null");
        }

        return personDAO
            .findById(personId)
            .orElseThrow(() -> new RuntimeException("Person not found with ID: " + personId));
    }

    @Override
    public Person getPersonByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        return personDAO
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Person not found with email: " + email));
    }

    @Override
    public List<Person> getAllPersons() {
        return personDAO.findAll();
    }

    @Override
    public void updatePerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        if (person.getPersonId() == null) {
            throw new IllegalArgumentException("Person ID cannot be null for update");
        }
        if (person.getFirstName() == null || person.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (person.getLastName() == null || person.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (person.getEmail() == null || person.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        personDAO.update(person);
        LOGGER.info("Person '{}' updated successfully", person.getFullName());
    }

    @Override
    public void deletePerson(Integer personId) {
        if (personId == null) {
            throw new IllegalArgumentException("Person ID cannot be null");
        }

        personDAO.delete(personId);
        LOGGER.info("Person with ID {} deleted successfully", personId);
    }

    @Override
    public boolean existsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return personDAO.findByEmail(email).isPresent();
    }
}
