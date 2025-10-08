package com.solvd.university.dao.interfaces;

import com.solvd.university.model.Professor;
import java.util.List;
import java.util.Optional;

public interface ProfessorDAO {
    void save(Professor professor);

    Optional<Professor> findById(Integer professorId);

    Optional<Professor> findByFullName(String firstName, String lastName);

    List<Professor> findAll();

    void update(Professor professor);

    void delete(Integer professorId);
}
