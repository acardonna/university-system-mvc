package com.solvd.university.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.dao.impl.MyBatisProgramDAOImpl;
import com.solvd.university.dao.interfaces.MyBatisProgramDAO;
import com.solvd.university.model.Department;
import com.solvd.university.model.Program;
import com.solvd.university.service.interfaces.MyBatisProgramService;

public class MyBatisProgramServiceImpl implements MyBatisProgramService {

    private static final Logger LOGGER = LogManager.getLogger(MyBatisProgramServiceImpl.class);
    private final MyBatisProgramDAO programDAO;

    public MyBatisProgramServiceImpl() {
        this.programDAO = new MyBatisProgramDAOImpl();
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

    @Override
    public void demonstrateCRUD() {
        List<Program> existingPrograms = getAllPrograms();
        Integer departmentId = null;
        Integer universityId = null;

        if (!existingPrograms.isEmpty()) {
            Program firstProgram = existingPrograms.get(0);
            departmentId = firstProgram.getDepartmentId();
            universityId = firstProgram.getUniversityId();
            LOGGER.info(
                "Using existing department_id: {} and university_id: {} from database",
                departmentId,
                universityId
            );
        } else {
            LOGGER.warn("No existing programs found. Please ensure database is initialized.");
        }

        Program testProgram1 = new Program();
        testProgram1.setName("MyBatis Program Alpha");
        testProgram1.setDuration(4);
        testProgram1.setPrice(45000.00);
        testProgram1.setDepartmentId(departmentId);
        testProgram1.setUniversityId(universityId);

        addProgram(testProgram1);
        LOGGER.info("Created first program: {} with ID: {}", testProgram1.getName(), testProgram1.getProgramId());

        Program testProgram2 = new Program();
        testProgram2.setName("MyBatis Program Beta");
        testProgram2.setDuration(3);
        testProgram2.setPrice(38000.00);
        testProgram2.setDepartmentId(departmentId);
        testProgram2.setUniversityId(universityId);

        addProgram(testProgram2);
        LOGGER.info("Created second program: {} with ID: {}", testProgram2.getName(), testProgram2.getProgramId());

        Program retrievedProgram = getProgramByName("MyBatis Program Alpha");
        if (retrievedProgram != null) {
            LOGGER.info("Retrieved program: {}", retrievedProgram.getName());

            retrievedProgram.setPrice(50000.00);
            updateProgram(retrievedProgram);
            LOGGER.info("Updated program price to: {}", retrievedProgram.getPrice());
        }

        if (testProgram2.getProgramId() != null) {
            deleteProgram(testProgram2.getProgramId());
            LOGGER.info("Deleted program: {} with ID: {}", testProgram2.getName(), testProgram2.getProgramId());
        }

        LOGGER.info("Program '{}' with ID: {} remains in database", testProgram1.getName(), testProgram1.getProgramId());
    }
}
