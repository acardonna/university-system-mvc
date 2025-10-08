package com.solvd.university.service.impl;

import com.solvd.university.dao.interfaces.ProfessorDAO;
import com.solvd.university.model.Course;
import com.solvd.university.model.Professor;
import com.solvd.university.service.interfaces.CourseService;
import com.solvd.university.service.interfaces.ProfessorService;
import java.util.List;

public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorDAO professorDAO;
    private final CourseService courseService;

    public ProfessorServiceImpl(ProfessorDAO professorDAO, CourseService courseService) {
        this.professorDAO = professorDAO;
        this.courseService = courseService;
    }

    @Override
    public void addProfessor(Professor professor) {
        professorDAO.save(professor);
    }

    @Override
    public Professor getProfessorByName(String firstName, String lastName) {
        Professor professor = professorDAO.findByFullName(firstName, lastName).orElse(null);
        if (professor != null) {
            loadProfessorCourses(professor);
        }
        return professor;
    }

    @Override
    public List<Professor> getAllProfessors() {
        List<Professor> professors = professorDAO.findAll();

        if (!professors.isEmpty()) {
            var allCourses = courseService.getAllCourses();
            professors.forEach(professor -> loadProfessorCoursesFromList(professor, allCourses));
        }
        return professors;
    }

    @Override
    public void updateProfessor(Professor professor) {
        professorDAO.update(professor);
    }

    @Override
    public void deleteProfessor(Integer professorId) {
        professorDAO.delete(professorId);
    }

    private void loadProfessorCourses(Professor professor) {
        if (professor.getProfessorId() != null) {
            var allCourses = courseService.getAllCourses();
            loadProfessorCoursesFromList(professor, allCourses);
        }
    }

    private void loadProfessorCoursesFromList(Professor professor, List<Course<?, ?>> allCourses) {
        if (professor.getProfessorId() != null) {
            allCourses
                .stream()
                .filter(
                    course ->
                        course.getProfessor() != null &&
                        course.getProfessor().getProfessorId() != null &&
                        course.getProfessor().getProfessorId().equals(professor.getProfessorId())
                )
                .forEach(professor::assignCourse);
        }
    }
}
