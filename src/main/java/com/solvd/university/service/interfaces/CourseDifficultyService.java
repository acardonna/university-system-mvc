package com.solvd.university.service.interfaces;

import com.solvd.university.model.CourseDifficulty;
import java.util.List;

public interface CourseDifficultyService {
    void addCourseDifficulty(CourseDifficulty difficulty);

    CourseDifficulty getCourseDifficultyById(Integer courseDifficultyId);

    CourseDifficulty getCourseDifficultyByDisplayName(String displayName);

    CourseDifficulty getCourseDifficultyByLevel(int level);

    List<CourseDifficulty> getAllCourseDifficulties();

    void updateCourseDifficulty(CourseDifficulty difficulty);

    void deleteCourseDifficulty(Integer courseDifficultyId);
}
