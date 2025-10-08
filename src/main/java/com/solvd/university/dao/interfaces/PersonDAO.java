package com.solvd.university.dao.interfaces;

import com.solvd.university.model.Person;
import java.util.List;
import java.util.Optional;

public interface PersonDAO {
    void save(Person person);

    Optional<Person> findById(Integer personId);

    Optional<Person> findByEmail(String email);

    List<Person> findAll();

    void update(Person person);

    void delete(Integer personId);
}
