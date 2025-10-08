package com.solvd.university.dao.interfaces;

import com.solvd.university.model.Department;
import com.solvd.university.model.Program;
import java.util.List;
import java.util.Optional;

public interface ProgramDAO {
    void save(Program program);

    Optional<Program> findById(Integer programId);

    Optional<Program> findByName(String name);

    List<Program> findByDepartment(Department<?> department);

    List<Program> findAll();

    void update(Program program);

    void delete(Integer programId);
}
