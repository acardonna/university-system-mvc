package com.solvd.university.service.impl;

import com.solvd.university.dao.interfaces.ProgramDAO;
import com.solvd.university.model.Department;
import com.solvd.university.model.Program;
import com.solvd.university.service.interfaces.ProgramService;
import java.util.List;

public class ProgramServiceImpl implements ProgramService {

    private final ProgramDAO programDAO;

    public ProgramServiceImpl(ProgramDAO programDAO) {
        this.programDAO = programDAO;
    }

    @Override
    public void addProgram(Program program) {
        programDAO.save(program);
    }

    @Override
    public Program getProgramByName(String name) {
        return programDAO.findByName(name).orElse(null);
    }

    @Override
    public List<Program> getProgramsByDepartment(Department<?> department) {
        return programDAO.findByDepartment(department);
    }

    @Override
    public List<Program> getAllPrograms() {
        return programDAO.findAll();
    }

    @Override
    public void updateProgram(Program program) {
        programDAO.update(program);
    }

    @Override
    public void deleteProgram(Integer programId) {
        programDAO.delete(programId);
    }
}
