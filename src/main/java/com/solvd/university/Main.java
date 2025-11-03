package com.solvd.university;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.service.factory.ServiceFactory;
import com.solvd.university.service.interfaces.*;
import com.solvd.university.util.DatabaseInitializer;
import com.solvd.university.view.UserInterface;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        LOGGER.info("Starting University System Application");

        // demonstrateMyBatis();
        // demonstrateJacksonSerialization();
        // demonstrateJAXBParsing();
        // demonstrateStAXParsing();

        UniversityService universityService = ServiceFactory.create(UniversityService.class);
        DepartmentService departmentService = ServiceFactory.create(DepartmentService.class);
        BuildingService buildingService = ServiceFactory.create(BuildingService.class);
        StudentGradeService studentGradeService = ServiceFactory.create(StudentGradeService.class);
        CourseGradeService courseGradeService = ServiceFactory.create(CourseGradeService.class);
        StudentService studentService = ServiceFactory.create(StudentService.class);
        CourseService courseService = ServiceFactory.create(CourseService.class);
        ProfessorService professorService = ServiceFactory.create(ProfessorService.class);
        ProgramService programService = ServiceFactory.create(ProgramService.class);
        EnrollmentService enrollmentService = ServiceFactory.create(EnrollmentService.class);
        ClassroomService classroomService = ServiceFactory.create(ClassroomService.class);
        CourseDifficultyService courseDifficultyService = ServiceFactory.create(CourseDifficultyService.class);
        EnrollmentStatusService enrollmentStatusService = ServiceFactory.create(EnrollmentStatusService.class);
        GradeLevelService gradeLevelService = ServiceFactory.create(GradeLevelService.class);
        PersonService personService = ServiceFactory.create(PersonService.class);
        StaffService staffService = ServiceFactory.create(StaffService.class);

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

    private static void demonstrateStAXParsing() {
        LOGGER.info("=== Demonstrating StAX XML Parsing ===");
        XmlParserService xmlParserService = ServiceFactory.create(XmlParserService.class);
        xmlParserService.displayDepartments();
        xmlParserService.displayBuildings();
    }

    private static void demonstrateJAXBParsing() {
        LOGGER.info("=== Demonstrating JAXB XML Parsing ===");
        XmlParserService xmlParserService = ServiceFactory.create(XmlParserService.class);
        xmlParserService.displayDepartmentsWithJAXB();
    }

    private static void demonstrateJacksonSerialization() {
        LOGGER.info("=== Demonstrating Jackson JSON Serialization ===");

        ProgramJsonService programService = ServiceFactory.create(ProgramJsonService.class);
        EnrollmentStatusJsonService statusService = ServiceFactory.create(EnrollmentStatusJsonService.class);

        programService.demonstrateReadWrite();
        LOGGER.info("");
        statusService.demonstrateReadWrite();
    }

    private static void demonstrateMyBatis() {
        LOGGER.info("=== Demonstrating MyBatis ORM ===");

        MyBatisProgramService myBatisProgramService = ServiceFactory.create(MyBatisProgramService.class);
        MyBatisEnrollmentStatusService myBatisEnrollmentStatusService = ServiceFactory.create(MyBatisEnrollmentStatusService.class);

        LOGGER.info("\n--- Testing Program CRUD Operations with MyBatis ---");
        LOGGER.info("Total programs before MyBatis operations: {}", myBatisProgramService.getAllPrograms().size());

        myBatisProgramService.demonstrateCRUD();

        LOGGER.info("Total programs after MyBatis operations: {}", myBatisProgramService.getAllPrograms().size());

        LOGGER.info("\n--- Testing EnrollmentStatus CRUD Operations with MyBatis ---");
        LOGGER.info(
            "Total enrollment statuses before MyBatis operations: {}",
            myBatisEnrollmentStatusService.getAllEnrollmentStatuses().size()
        );

        myBatisEnrollmentStatusService.demonstrateCRUD();

        LOGGER.info(
            "Total enrollment statuses after MyBatis operations: {}",
            myBatisEnrollmentStatusService.getAllEnrollmentStatuses().size()
        );

        LOGGER.info("\n=== MyBatis Demonstration Completed ===");
    }
}
