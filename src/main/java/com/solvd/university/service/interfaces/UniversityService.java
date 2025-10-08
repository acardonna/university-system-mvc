package com.solvd.university.service.interfaces;

import com.solvd.university.model.University;
import java.util.List;

public interface UniversityService {
    University addUniversity(String name);

    University getUniversityById(Integer universityId);

    University getUniversityByName(String name);

    List<University> getAllUniversities();

    void updateUniversity(University university);

    void deleteUniversity(Integer universityId);
}
