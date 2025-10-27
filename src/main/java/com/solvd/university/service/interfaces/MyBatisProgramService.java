package com.solvd.university.service.interfaces;

import java.util.List;

import com.solvd.university.model.Department;
import com.solvd.university.model.Program;

public interface MyBatisProgramService {
    void addProgram(Program program);

    Program getProgramByName(String name);

    List<Program> getProgramsByDepartment(Department<?> department);

    List<Program> getAllPrograms();

    void updateProgram(Program program);

    void deleteProgram(Integer programId);

    void demonstrateCRUD();
}
