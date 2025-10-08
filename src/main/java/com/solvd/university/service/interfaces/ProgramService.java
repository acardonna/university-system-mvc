package com.solvd.university.service.interfaces;

import com.solvd.university.model.Department;
import com.solvd.university.model.Program;
import java.util.List;

public interface ProgramService {
    void addProgram(Program program);

    Program getProgramByName(String name);

    List<Program> getProgramsByDepartment(Department<?> department);

    List<Program> getAllPrograms();

    void updateProgram(Program program);

    void deleteProgram(Integer programId);
}
