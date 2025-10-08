package com.solvd.university.service.impl;

import com.solvd.university.dao.interfaces.CourseGradeDAO;
import com.solvd.university.model.CourseGrade;
import com.solvd.university.service.interfaces.CourseGradeService;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CourseGradeServiceImpl implements CourseGradeService {

    private static final Logger LOGGER = LogManager.getLogger();
    private final CourseGradeDAO courseGradeDAO;

    public CourseGradeServiceImpl(CourseGradeDAO courseGradeDAO) {
        this.courseGradeDAO = courseGradeDAO;
    }

    @Override
    public CourseGrade addGrade(
        Integer courseId,
        String subject,
        Double value,
        Integer semester,
        LocalDateTime recordedAt
    ) {
        if (courseId == null) {
            throw new IllegalArgumentException("Course ID cannot be null");
        }
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be null or empty");
        }
        if (value == null || value < 0 || value > 100) {
            throw new IllegalArgumentException("Grade value must be between 0 and 100");
        }
        if (semester == null || semester < 1) {
            throw new IllegalArgumentException("Semester must be a positive number");
        }

        LocalDateTime recorded = recordedAt != null ? recordedAt : LocalDateTime.now();

        CourseGrade grade = new CourseGrade(courseId, subject, value, semester, recorded);
        courseGradeDAO.save(grade);

        LOGGER.info("Added grade for course {}: {} = {} (semester {})", courseId, subject, value, semester);

        return grade;
    }

    @Override
    public CourseGrade getGradeById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Grade ID cannot be null");
        }
        return courseGradeDAO.findById(id);
    }

    @Override
    public List<CourseGrade> getAllGrades() {
        return courseGradeDAO.findAll();
    }

    @Override
    public List<CourseGrade> getGradesByCourse(Integer courseId) {
        if (courseId == null) {
            throw new IllegalArgumentException("Course ID cannot be null");
        }
        return courseGradeDAO.findByCourseId(courseId);
    }

    @Override
    public List<CourseGrade> getGradesByCourseAndSemester(Integer courseId, Integer semester) {
        if (courseId == null) {
            throw new IllegalArgumentException("Course ID cannot be null");
        }
        if (semester == null) {
            throw new IllegalArgumentException("Semester cannot be null");
        }
        return courseGradeDAO.findByCourseIdAndSemester(courseId, semester);
    }

    @Override
    public double calculateCourseAverage(Integer courseId) {
        if (courseId == null) {
            throw new IllegalArgumentException("Course ID cannot be null");
        }

        List<CourseGrade> grades = courseGradeDAO.findByCourseId(courseId);

        if (grades.isEmpty()) {
            return 0.0;
        }

        double sum = grades.stream().mapToDouble(CourseGrade::getValue).sum();

        return sum / grades.size();
    }

    @Override
    public void updateGrade(CourseGrade grade) {
        if (grade == null) {
            throw new IllegalArgumentException("Grade cannot be null");
        }
        if (grade.getCourseGradeId() == null) {
            throw new IllegalArgumentException("Grade ID cannot be null for update");
        }

        courseGradeDAO.update(grade);
        LOGGER.info("Updated course grade: ID {}", grade.getCourseGradeId());
    }

    @Override
    public void deleteGrade(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Grade ID cannot be null");
        }

        courseGradeDAO.delete(id);
        LOGGER.info("Deleted course grade: ID {}", id);
    }

    @Override
    public boolean gradeExists(Integer id) {
        if (id == null) {
            return false;
        }
        return courseGradeDAO.findById(id) != null;
    }
}
