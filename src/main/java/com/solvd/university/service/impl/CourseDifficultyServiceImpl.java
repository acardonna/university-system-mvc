package com.solvd.university.service.impl;

import com.solvd.university.dao.interfaces.CourseDifficultyDAO;
import com.solvd.university.model.CourseDifficulty;
import com.solvd.university.service.interfaces.CourseDifficultyService;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CourseDifficultyServiceImpl implements CourseDifficultyService {

    private static final Logger LOGGER = LogManager.getLogger(CourseDifficultyServiceImpl.class);
    private final CourseDifficultyDAO courseDifficultyDAO;

    public CourseDifficultyServiceImpl(CourseDifficultyDAO courseDifficultyDAO) {
        this.courseDifficultyDAO = courseDifficultyDAO;
    }

    @Override
    public CourseDifficulty getCourseDifficultyById(Integer courseDifficultyId) {
        if (courseDifficultyId == null) {
            throw new IllegalArgumentException("Course difficulty ID cannot be null");
        }

        return courseDifficultyDAO
            .findById(courseDifficultyId)
            .orElseThrow(() -> new RuntimeException("Course difficulty not found with ID: " + courseDifficultyId));
    }

    @Override
    public CourseDifficulty getCourseDifficultyByDisplayName(String displayName) {
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Display name cannot be null or empty");
        }

        return courseDifficultyDAO
            .findByDisplayName(displayName)
            .orElseThrow(() -> new RuntimeException("Course difficulty not found with display name: " + displayName));
    }

    @Override
    public CourseDifficulty getCourseDifficultyByLevel(int level) {
        return courseDifficultyDAO
            .findByLevel(level)
            .orElseThrow(() -> new RuntimeException("Course difficulty not found with level: " + level));
    }

    @Override
    public List<CourseDifficulty> getAllCourseDifficulties() {
        return courseDifficultyDAO.findAll();
    }

    @Override
    public void addCourseDifficulty(CourseDifficulty difficulty) {
        if (difficulty == null) {
            throw new IllegalArgumentException("Course difficulty cannot be null");
        }
        courseDifficultyDAO.save(difficulty);
    }

    @Override
    public void updateCourseDifficulty(CourseDifficulty difficulty) {
        if (difficulty == null) {
            throw new IllegalArgumentException("Course difficulty cannot be null");
        }
        if (difficulty.getCourseDifficultyId() == null) {
            throw new IllegalArgumentException("Course difficulty ID cannot be null for update");
        }
        courseDifficultyDAO.update(difficulty);
        LOGGER.info("Updated course difficulty with ID: {}", difficulty.getCourseDifficultyId());
    }

    @Override
    public void deleteCourseDifficulty(Integer courseDifficultyId) {
        if (courseDifficultyId == null) {
            throw new IllegalArgumentException("Course difficulty ID cannot be null");
        }
        courseDifficultyDAO.delete(courseDifficultyId);
        LOGGER.info("Deleted course difficulty with ID: {}", courseDifficultyId);
    }
}
