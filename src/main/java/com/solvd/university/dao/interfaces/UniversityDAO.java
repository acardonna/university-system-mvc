package com.solvd.university.dao.interfaces;

import com.solvd.university.model.University;
import java.util.List;
import java.util.Optional;

public interface UniversityDAO {
    void save(University university);

    Optional<University> findById(Integer universityId);

    Optional<University> findByName(String name);

    List<University> findAll();

    void update(University university);

    void delete(Integer universityId);
}
