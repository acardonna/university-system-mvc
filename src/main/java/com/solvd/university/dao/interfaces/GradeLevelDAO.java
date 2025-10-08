package com.solvd.university.dao.interfaces;

import com.solvd.university.model.GradeLevel;
import java.util.List;
import java.util.Optional;

public interface GradeLevelDAO {
    void save(GradeLevel level);

    Optional<GradeLevel> findById(Integer gradeLevelId);

    Optional<GradeLevel> findByDisplayName(String displayName);

    Optional<GradeLevel> findByYear(int year);

    List<GradeLevel> findAll();

    void update(GradeLevel level);

    void delete(Integer gradeLevelId);
}
