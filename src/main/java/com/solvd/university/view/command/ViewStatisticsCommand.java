package com.solvd.university.view.command;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.model.Course;
import com.solvd.university.model.Department;
import com.solvd.university.service.interfaces.*;

public class ViewStatisticsCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ViewStatisticsCommand.class);

    private final DepartmentService departmentService;
    private final ProfessorService professorService;
    private final CourseService courseService;

    public ViewStatisticsCommand(DepartmentService departmentService,
                                  ProfessorService professorService,
                                  CourseService courseService) {
        this.departmentService = departmentService;
        this.professorService = professorService;
        this.courseService = courseService;
    }

    @Override
    public void execute() {
        showProfessorsAndCourses();
    }

    @Override
    public String getDescription() {
        return "View statistics (professors and courses)";
    }

    private void showProfessorsAndCourses() {
        LOGGER.info("=== University Statistics ===");
        LOGGER.info("");

        List<Department<?>> departments = departmentService.getAllDepartments();
        LOGGER.info(String.format("Total Departments: %d", departments.size()));
        LOGGER.info(String.format("Total Professors: %d", professorService.getAllProfessors().size()));
        LOGGER.info(String.format("Total Courses: %d", courseService.getAllCourses().size()));
        LOGGER.info("");

        LOGGER.info("=== Departments and Their Courses ===");
        LOGGER.info("");

        departments
            .forEach(dept -> {
                LOGGER.info("Department: " + dept.getName());

                List<Course<?, ?>> coursesByDept = courseService
                    .getAllCourses()
                    .stream()
                    .filter(c -> c.getDepartment() != null && c.getDepartment().equals(dept))
                    .collect(Collectors.toList());

                if (coursesByDept.isEmpty()) {
                    LOGGER.info("  No courses available");
                } else {
                    coursesByDept
                        .forEach(course -> {
                            LOGGER.info(
                                "  - " +
                                    course.getCourseCode() +
                                    ": " +
                                    course.getCourseName() +
                                    " (" +
                                    course.getCreditHours() +
                                    " credits)"
                            );
                        });
                }
                LOGGER.info("");
            });

        LOGGER.info("=== Professors ===");
        LOGGER.info("");

        Map<Object, List<Course<?, ?>>> coursesByProfessor = courseService
            .getAllCourses()
            .stream()
            .filter(c -> c.getProfessor() != null)
            .collect(Collectors.groupingBy(Course::getProfessor));

        coursesByProfessor
            .entrySet()
            .forEach(entry -> {
                Object professor = entry.getKey();
                LOGGER.info("Professor: " + professor);
                LOGGER.info("Courses Taught: " + entry.getValue().size());
                entry
                    .getValue()
                    .forEach(course -> {
                        LOGGER.info(
                            "  - " +
                                course.getCourseCode() +
                                ": " +
                                course.getCourseName()
                        );
                    });
                LOGGER.info("");
            });
    }
}
