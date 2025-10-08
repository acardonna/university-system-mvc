package com.solvd.university.service.interfaces;

import com.solvd.university.model.Person;
import java.util.List;

public interface PersonService {
    Person addPerson(String firstName, String lastName, String email);

    Person getPersonById(Integer personId);

    Person getPersonByEmail(String email);

    List<Person> getAllPersons();

    void updatePerson(Person person);

    void deletePerson(Integer personId);

    boolean existsByEmail(String email);
}
