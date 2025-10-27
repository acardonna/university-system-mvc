package com.solvd.university;

import com.solvd.university.service.impl.*;
import com.solvd.university.service.interfaces.*;
import com.solvd.university.util.DatabaseInitializer;
import com.solvd.university.view.UserInterface;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Starting University System Application");

        demonstrateMyBatis();
        // demonstrateJacksonSerialization();
        // demonstrateJAXBParsing();
        // demonstrateStAXParsing();

        // UniversityService universityService = new UniversityServiceImpl();
        // DepartmentService departmentService = new DepartmentServiceImpl();
        // BuildingService buildingService = new BuildingServiceImpl();
        // StudentGradeService studentGradeService = new StudentGradeServiceImpl();
        // CourseGradeService courseGradeService = new CourseGradeServiceImpl();
        // StudentService studentService = new StudentServiceImpl();
        // CourseService courseService = new CourseServiceImpl();
        // ProfessorService professorService = new ProfessorServiceImpl();
        // ProgramService programService = new ProgramServiceImpl();
        // EnrollmentService enrollmentService = new EnrollmentServiceImpl();
        // ClassroomService classroomService = new ClassroomServiceImpl();
        // CourseDifficultyService courseDifficultyService = new CourseDifficultyServiceImpl();
        // EnrollmentStatusService enrollmentStatusService = new EnrollmentStatusServiceImpl();
        // GradeLevelService gradeLevelService = new GradeLevelServiceImpl();
        // PersonService personService = new PersonServiceImpl();
        // StaffService staffService = new StaffServiceImpl();

        // Scanner scanner = new Scanner(System.in);

        // DatabaseInitializer.initializeAll(
        //     universityService,
        //     departmentService,
        //     buildingService,
        //     courseDifficultyService,
        //     enrollmentStatusService,
        //     gradeLevelService,
        //     programService,
        //     professorService,
        //     courseService,
        //     classroomService,
        //     studentService,
        //     enrollmentService,
        //     studentGradeService,
        //     courseGradeService,
        //     personService,
        //     staffService
        // );

    //     DatabaseInitializer.displayDatabaseOverview(
    //         universityService,
    //         departmentService,
    //         buildingService,
    //         programService,
    //         professorService,
    //         courseService,
    //         classroomService,
    //         studentService,
    //         enrollmentService,
    //         courseDifficultyService,
    //         enrollmentStatusService,
    //         gradeLevelService,
    //         studentGradeService,
    //         courseGradeService,
    //         personService,
    //         staffService
    //     );

    //     UserInterface userInterface = new UserInterface(
    //         scanner,
    //         "Oxford",
    //         universityService,
    //         departmentService,
    //         buildingService,
    //         studentService,
    //         enrollmentService,
    //         programService,
    //         professorService,
    //         courseService,
    //         classroomService,
    //         studentGradeService,
    //         courseGradeService
    //     );
    //     userInterface.start();
    }

    private static void demonstrateStAXParsing() {
        LOGGER.info("=== Demonstrating StAX XML Parsing ===");
        XmlParserService xmlParserService = new XmlParserServiceImpl();
        xmlParserService.displayDepartments();
        xmlParserService.displayBuildings();
    }

    private static void demonstrateJAXBParsing() {
        LOGGER.info("=== Demonstrating JAXB XML Parsing ===");
        XmlParserService xmlParserService = new XmlParserServiceImpl();
        xmlParserService.displayDepartmentsWithJAXB();
    }

    private static void demonstrateJacksonSerialization() {
        LOGGER.info("=== Demonstrating Jackson JSON Serialization ===");

        ProgramJsonService programService = new ProgramJsonServiceImpl();
        EnrollmentStatusJsonService statusService = new EnrollmentStatusJsonServiceImpl();

        programService.demonstrateReadWrite();
        LOGGER.info("");
        statusService.demonstrateReadWrite();
    }

    private static void demonstrateMyBatis() {
        LOGGER.info("=== Demonstrating MyBatis ORM ===");

        MyBatisProgramService myBatisProgramService = new MyBatisProgramServiceImpl();
        MyBatisEnrollmentStatusService myBatisEnrollmentStatusService = new MyBatisEnrollmentStatusServiceImpl();

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
