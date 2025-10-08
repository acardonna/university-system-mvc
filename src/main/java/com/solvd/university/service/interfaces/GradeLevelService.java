package com.solvd.university.service.interfaces;

import com.solvd.university.model.GradeLevel;
import java.util.List;

public interface GradeLevelService {
    void addGradeLevel(GradeLevel level);

    GradeLevel getGradeLevelById(Integer gradeLevelId);

    GradeLevel getGradeLevelByDisplayName(String displayName);

    GradeLevel getGradeLevelByYear(int year);

    List<GradeLevel> getAllGradeLevels();

    void updateGradeLevel(GradeLevel level);

    void deleteGradeLevel(Integer gradeLevelId);
}
