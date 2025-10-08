package com.solvd.university.dao.interfaces;

import com.solvd.university.model.Department;
import java.util.List;
import java.util.Optional;

public interface DepartmentDAO {
    void save(Department<?> department);

    Optional<Department<?>> findById(Integer departmentId);

    Optional<Department<?>> findByCode(String code);

    List<Department<?>> findAll();

    List<Department<?>> findByUniversity(Integer universityId);

    void update(Department<?> department);

    void delete(Integer departmentId);
}
