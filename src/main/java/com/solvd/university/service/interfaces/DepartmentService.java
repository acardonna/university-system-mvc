package com.solvd.university.service.interfaces;

import com.solvd.university.model.Department;
import java.util.List;

public interface DepartmentService {
    Department<?> addDepartment(Department<?> department);

    Department<?> getDepartmentById(Integer departmentId);

    Department<?> getDepartmentByCode(String code);

    List<Department<?>> getAllDepartments();

    List<Department<?>> getDepartmentsByUniversity(Integer universityId);

    void updateDepartment(Department<?> department);

    void deleteDepartment(Integer departmentId);
}
