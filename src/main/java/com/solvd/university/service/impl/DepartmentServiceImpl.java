package com.solvd.university.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.dao.factory.DAOFactory;
import com.solvd.university.dao.interfaces.DepartmentDAO;
import com.solvd.university.model.Department;
import com.solvd.university.service.interfaces.DepartmentService;

public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger LOGGER = LogManager.getLogger(DepartmentServiceImpl.class);
    private final DepartmentDAO departmentDAO;

    public DepartmentServiceImpl() {
        this.departmentDAO = DAOFactory.create(DepartmentDAO.class);
    }

    @Override
    public Department<?> addDepartment(Department<?> department) {
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null");
        }
        if (department.getName() == null || department.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be null or empty");
        }
        if (department.getDepartmentCode() == null) {
            throw new IllegalArgumentException("Department code cannot be null");
        }

        Optional<Department<?>> existing = departmentDAO.findByCode(department.getDepartmentCode().toString());
        if (existing.isPresent()) {
            LOGGER.warn("Department with code '{}' already exists", department.getDepartmentCode());
            return existing.get();
        }

        departmentDAO.save(department);
        LOGGER.info("Department '{}' added successfully", department.getName());
        return department;
    }

    @Override
    public Department<?> getDepartmentById(Integer departmentId) {
        if (departmentId == null) {
            throw new IllegalArgumentException("Department ID cannot be null");
        }

        return departmentDAO
            .findById(departmentId)
            .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));
    }

    @Override
    public Department<?> getDepartmentByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Department code cannot be null or empty");
        }

        return departmentDAO
            .findByCode(code)
            .orElseThrow(() -> new RuntimeException("Department not found with code: " + code));
    }

    @Override
    public List<Department<?>> getAllDepartments() {
        return departmentDAO.findAll();
    }

    @Override
    public List<Department<?>> getDepartmentsByUniversity(Integer universityId) {
        if (universityId == null) {
            throw new IllegalArgumentException("University ID cannot be null");
        }

        return departmentDAO.findByUniversity(universityId);
    }

    @Override
    public void updateDepartment(Department<?> department) {
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null");
        }
        if (department.getDepartmentId() == null) {
            throw new IllegalArgumentException("Department ID cannot be null for update");
        }
        if (department.getName() == null || department.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be null or empty");
        }

        departmentDAO.update(department);
        LOGGER.info("Department '{}' updated successfully", department.getName());
    }

    @Override
    public void deleteDepartment(Integer departmentId) {
        if (departmentId == null) {
            throw new IllegalArgumentException("Department ID cannot be null");
        }

        departmentDAO.delete(departmentId);
        LOGGER.info("Department with ID {} deleted successfully", departmentId);
    }
}
