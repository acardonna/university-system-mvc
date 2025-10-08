package com.solvd.university.dao.interfaces;

import com.solvd.university.model.CourseDifficulty;
import java.util.List;
import java.util.Optional;

public interface CourseDifficultyDAO {
    void save(CourseDifficulty difficulty);

    Optional<CourseDifficulty> findById(Integer courseDifficultyId);

    Optional<CourseDifficulty> findByDisplayName(String displayName);

    Optional<CourseDifficulty> findByLevel(int level);

    List<CourseDifficulty> findAll();

    void update(CourseDifficulty difficulty);

    void delete(Integer courseDifficultyId);
}
