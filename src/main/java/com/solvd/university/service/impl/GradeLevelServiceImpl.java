package com.solvd.university.service.impl;

import com.solvd.university.dao.impl.GradeLevelDAOImpl;
import com.solvd.university.dao.interfaces.GradeLevelDAO;
import com.solvd.university.model.GradeLevel;
import com.solvd.university.service.interfaces.GradeLevelService;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GradeLevelServiceImpl implements GradeLevelService {

    private static final Logger LOGGER = LogManager.getLogger(GradeLevelServiceImpl.class);
    private final GradeLevelDAO gradeLevelDAO;

    public GradeLevelServiceImpl() {
        this.gradeLevelDAO = new GradeLevelDAOImpl();
    }

    @Override
    public GradeLevel getGradeLevelById(Integer gradeLevelId) {
        if (gradeLevelId == null) {
            throw new IllegalArgumentException("Grade level ID cannot be null");
        }

        return gradeLevelDAO
            .findById(gradeLevelId)
            .orElseThrow(() -> new RuntimeException("Grade level not found with ID: " + gradeLevelId));
    }

    @Override
    public GradeLevel getGradeLevelByDisplayName(String displayName) {
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Display name cannot be null or empty");
        }

        return gradeLevelDAO
            .findByDisplayName(displayName)
            .orElseThrow(() -> new RuntimeException("Grade level not found with display name: " + displayName));
    }

    @Override
    public GradeLevel getGradeLevelByYear(int year) {
        return gradeLevelDAO
            .findByYear(year)
            .orElseThrow(() -> new RuntimeException("Grade level not found with year: " + year));
    }

    @Override
    public List<GradeLevel> getAllGradeLevels() {
        return gradeLevelDAO.findAll();
    }

    @Override
    public void addGradeLevel(GradeLevel level) {
        if (level == null) {
            throw new IllegalArgumentException("Grade level cannot be null");
        }
        gradeLevelDAO.save(level);
    }

    @Override
    public void updateGradeLevel(GradeLevel level) {
        if (level == null) {
            throw new IllegalArgumentException("Grade level cannot be null");
        }
        if (level.getGradeLevelId() == null) {
            throw new IllegalArgumentException("Grade level ID cannot be null for update");
        }
        gradeLevelDAO.update(level);
        LOGGER.info("Updated grade level with ID: {}", level.getGradeLevelId());
    }

    @Override
    public void deleteGradeLevel(Integer gradeLevelId) {
        if (gradeLevelId == null) {
            throw new IllegalArgumentException("Grade level ID cannot be null");
        }
        gradeLevelDAO.delete(gradeLevelId);
        LOGGER.info("Deleted grade level with ID: {}", gradeLevelId);
    }
}
