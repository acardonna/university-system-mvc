package com.solvd.university.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.dao.factory.DAOFactory;
import com.solvd.university.dao.interfaces.UniversityDAO;
import com.solvd.university.model.University;
import com.solvd.university.service.interfaces.UniversityService;

public class UniversityServiceImpl implements UniversityService {

    private static final Logger LOGGER = LogManager.getLogger(UniversityServiceImpl.class);
    private final UniversityDAO universityDAO;

    public UniversityServiceImpl() {
        this.universityDAO = DAOFactory.create(UniversityDAO.class);
    }

    @Override
    public University addUniversity(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("University name cannot be null or empty");
        }

        Optional<University> existing = universityDAO.findByName(name);
        if (existing.isPresent()) {
            LOGGER.warn("University with name '{}' already exists", name);
            return existing.get();
        }

        University university = new University(name);
        universityDAO.save(university);
        LOGGER.info("University '{}' added successfully", name);
        return university;
    }

    @Override
    public University getUniversityById(Integer universityId) {
        if (universityId == null) {
            throw new IllegalArgumentException("University ID cannot be null");
        }

        return universityDAO
            .findById(universityId)
            .orElseThrow(() -> new RuntimeException("University not found with ID: " + universityId));
    }

    @Override
    public University getUniversityByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("University name cannot be null or empty");
        }

        return universityDAO
            .findByName(name)
            .orElseThrow(() -> new RuntimeException("University not found with name: " + name));
    }

    @Override
    public List<University> getAllUniversities() {
        return universityDAO.findAll();
    }

    @Override
    public void updateUniversity(University university) {
        if (university == null) {
            throw new IllegalArgumentException("University cannot be null");
        }
        if (university.getUniversityId() == null) {
            throw new IllegalArgumentException("University ID cannot be null for update");
        }
        if (university.getName() == null || university.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("University name cannot be null or empty");
        }

        universityDAO.update(university);
        LOGGER.info("University '{}' updated successfully", university.getName());
    }

    @Override
    public void deleteUniversity(Integer universityId) {
        if (universityId == null) {
            throw new IllegalArgumentException("University ID cannot be null");
        }

        universityDAO.delete(universityId);
        LOGGER.info("University with ID {} deleted successfully", universityId);
    }
}
