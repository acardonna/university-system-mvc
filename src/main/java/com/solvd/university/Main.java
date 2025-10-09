package com.solvd.university;

import com.solvd.university.service.impl.*;
import com.solvd.university.service.interfaces.*;
import com.solvd.university.util.DatabaseInitializer;
import com.solvd.university.view.UserInterface;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        UniversityService universityService = new UniversityServiceImpl();
        DepartmentService departmentService = new DepartmentServiceImpl();
        BuildingService buildingService = new BuildingServiceImpl();
        StudentGradeService studentGradeService = new StudentGradeServiceImpl();
        CourseGradeService courseGradeService = new CourseGradeServiceImpl();
        StudentService studentService = new StudentServiceImpl();
        CourseService courseService = new CourseServiceImpl();
        ProfessorService professorService = new ProfessorServiceImpl();
        ProgramService programService = new ProgramServiceImpl();
        EnrollmentService enrollmentService = new EnrollmentServiceImpl();
        ClassroomService classroomService = new ClassroomServiceImpl();
        CourseDifficultyService courseDifficultyService = new CourseDifficultyServiceImpl();
        EnrollmentStatusService enrollmentStatusService = new EnrollmentStatusServiceImpl();
        GradeLevelService gradeLevelService = new GradeLevelServiceImpl();
        PersonService personService = new PersonServiceImpl();
        StaffService staffService = new StaffServiceImpl();

        Scanner scanner = new Scanner(System.in);

        DatabaseInitializer.initializeAll(
            universityService,
            departmentService,
            buildingService,
            courseDifficultyService,
            enrollmentStatusService,
            gradeLevelService,
            programService,
            professorService,
            courseService,
            classroomService,
            studentService,
            enrollmentService,
            studentGradeService,
            courseGradeService,
            personService,
            staffService
        );

        DatabaseInitializer.displayDatabaseOverview(
            universityService,
            departmentService,
            buildingService,
            programService,
            professorService,
            courseService,
            classroomService,
            studentService,
            enrollmentService,
            courseDifficultyService,
            enrollmentStatusService,
            gradeLevelService,
            studentGradeService,
            courseGradeService,
            personService,
            staffService
        );

        UserInterface userInterface = new UserInterface(
            scanner,
            "Oxford",
            universityService,
            departmentService,
            buildingService,
            studentService,
            enrollmentService,
            programService,
            professorService,
            courseService,
            classroomService,
            studentGradeService,
            courseGradeService
        );
        userInterface.start();
    }
}
