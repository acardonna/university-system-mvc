package com.solvd.university.util;

import java.util.List;

import com.solvd.university.model.*;
import com.solvd.university.service.interfaces.*;

public class DatabaseInitializer {

    public static void initializeAll(
        UniversityService universityService,
        DepartmentService departmentService,
        BuildingService buildingService,
        CourseDifficultyService courseDifficultyService,
        EnrollmentStatusService enrollmentStatusService,
        GradeLevelService gradeLevelService,
        ProgramService programService,
        ProfessorService professorService,
        CourseService courseService,
        ClassroomService classroomService,
        StudentService studentService,
        EnrollmentService enrollmentService,
        StudentGradeService studentGradeService,
        CourseGradeService courseGradeService,
        PersonService personService,
        StaffService staffService
    ) {
        initializeUniversities(universityService);
        initializeDepartments(departmentService, universityService);
        initializeBuildings(buildingService, universityService);
        initializeCourseDifficulties(courseDifficultyService);
        initializeEnrollmentStatuses(enrollmentStatusService);
        initializeGradeLevels(gradeLevelService);
        initializePrograms(programService, departmentService);
        initializeProfessors(professorService, departmentService);
        initializeCourses(courseService, professorService, departmentService);
        initializeClassrooms(classroomService, buildingService);
        initializeStudents(studentService);
        initializeEnrollments(enrollmentService, studentService, programService);
        initializeStudentGrades(studentGradeService);
        initializeCourseGrades(courseGradeService);
        initializePersons(personService);
        initializeStaff(staffService);
    }

    public static void displayDatabaseOverview(
        UniversityService universityService,
        DepartmentService departmentService,
        BuildingService buildingService,
        ProgramService programService,
        ProfessorService professorService,
        CourseService courseService,
        ClassroomService classroomService,
        StudentService studentService,
        EnrollmentService enrollmentService,
        CourseDifficultyService courseDifficultyService,
        EnrollmentStatusService enrollmentStatusService,
        GradeLevelService gradeLevelService,
        StudentGradeService studentGradeService,
        CourseGradeService courseGradeService,
        PersonService personService,
        StaffService staffService
    ) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                   DATABASE OVERVIEW");
        System.out.println("=".repeat(60));

        int universityCount = universityService.getAllUniversities().size();
        int departmentCount = departmentService.getAllDepartments().size();
        int buildingCount = buildingService.getAllBuildings().size();
        int programCount = programService.getAllPrograms().size();
        int professorCount = professorService.getAllProfessors().size();
        int courseCount = courseService.getAllCourses().size();
        int classroomCount = classroomService.getAllClassrooms().size();
        int studentCount = studentService.getAllStudents().size();
        int enrollmentCount = enrollmentService.getAllEnrollments().size();
        int difficultyCount = courseDifficultyService.getAllCourseDifficulties().size();
        int statusCount = enrollmentStatusService.getAllEnrollmentStatuses().size();
        int gradeLevelCount = gradeLevelService.getAllGradeLevels().size();
        int studentGradeCount = studentGradeService.getAllGrades().size();
        int courseGradeCount = courseGradeService.getAllGrades().size();
        int personCount = personService.getAllPersons().size();
        int staffCount = staffService.getAllStaff().size();

        System.out.println(String.format("  %-30s %10d records", "Universities", universityCount));
        System.out.println(String.format("  %-30s %10d records", "Departments", departmentCount));
        System.out.println(String.format("  %-30s %10d records", "Buildings", buildingCount));
        System.out.println(String.format("  %-30s %10d records", "Classrooms", classroomCount));
        System.out.println(String.format("  %-30s %10d records", "Programs", programCount));
        System.out.println(String.format("  %-30s %10d records", "Courses", courseCount));
        System.out.println(String.format("  %-30s %10d records", "Professors", professorCount));
        System.out.println(String.format("  %-30s %10d records", "Students", studentCount));
        System.out.println(String.format("  %-30s %10d records", "Enrollments", enrollmentCount));
        System.out.println(String.format("  %-30s %10d records", "Student Grades", studentGradeCount));
        System.out.println(String.format("  %-30s %10d records", "Course Grades", courseGradeCount));
        System.out.println(String.format("  %-30s %10d records", "Persons", personCount));
        System.out.println(String.format("  %-30s %10d records", "Staff", staffCount));
        System.out.println(String.format("  %-30s %10d records", "Course Difficulties", difficultyCount));
        System.out.println(String.format("  %-30s %10d records", "Enrollment Statuses", statusCount));
        System.out.println(String.format("  %-30s %10d records", "Grade Levels", gradeLevelCount));

        int totalRecords =
            universityCount +
            departmentCount +
            buildingCount +
            programCount +
            professorCount +
            courseCount +
            classroomCount +
            studentCount +
            enrollmentCount +
            difficultyCount +
            statusCount +
            gradeLevelCount +
            studentGradeCount +
            courseGradeCount +
            personCount +
            staffCount;

        System.out.println("\n" + "-".repeat(60));
        System.out.println(String.format("  %-30s %10d records", "TOTAL", totalRecords));
        System.out.println("=".repeat(60) + "\n");
    }

    private static void initializeUniversities(UniversityService universityService) {
        printInitStart("University");

        if (!universityService.getAllUniversities().isEmpty()) {
            printInitSkip("University", true);
            return;
        }

        universityService.addUniversity("University of Oxford");
        universityService.addUniversity("University of Cambridge");
        universityService.addUniversity("Harvard University");
        universityService.addUniversity("Stanford University");
        universityService.addUniversity("Massachusetts Institute of Technology");
        universityService.addUniversity("Yale University");
        universityService.addUniversity("Princeton University");
        universityService.addUniversity("Columbia University");
        universityService.addUniversity("University of Chicago");
        universityService.addUniversity("University of California, Berkeley");

        List<University> universities = universityService.getAllUniversities();
        printInitComplete("University", universities.size(), "universities");
    }

    private static void initializeDepartments(
        DepartmentService departmentService,
        UniversityService universityService
    ) {
        printInitStart("Department");

        if (!departmentService.getAllDepartments().isEmpty()) {
            printInitSkip("Department", true);
            return;
        }

        List<University> universities = universityService.getAllUniversities();
        if (universities.isEmpty()) {
            printInitSkip("Department", false);
            System.out.println("    (Universities not found)");
            return;
        }

        University oxfordUniversity = universities.get(0);

        ComputerScienceDepartment csDept = new ComputerScienceDepartment();
        csDept.setUniversityId(oxfordUniversity.getUniversityId());
        departmentService.addDepartment(csDept);

        MathematicsDepartment mathDept = new MathematicsDepartment();
        mathDept.setUniversityId(oxfordUniversity.getUniversityId());
        departmentService.addDepartment(mathDept);

        EngineeringDepartment engDept = new EngineeringDepartment();
        engDept.setUniversityId(oxfordUniversity.getUniversityId());
        departmentService.addDepartment(engDept);

        BusinessDepartment busDept = new BusinessDepartment();
        busDept.setUniversityId(oxfordUniversity.getUniversityId());
        departmentService.addDepartment(busDept);

        ArtsDepartment artsDept = new ArtsDepartment();
        artsDept.setUniversityId(oxfordUniversity.getUniversityId());
        departmentService.addDepartment(artsDept);

        List<Department<?>> departments = departmentService.getAllDepartments();
        printInitComplete("Department", departments.size(), "departments");
    }

    private static void initializeBuildings(BuildingService buildingService, UniversityService universityService) {
        printInitStart("Building");

        if (!buildingService.getAllBuildings().isEmpty()) {
            printInitSkip("Building", true);
            return;
        }

        List<University> universities = universityService.getAllUniversities();
        if (universities.isEmpty()) {
            printInitSkip("Building", false);
            System.out.println("    (Universities not found)");
            return;
        }

        buildingService.addBuilding("Oxford Main Hall", universities.get(0).getUniversityId());
        if (universities.size() > 1) buildingService.addBuilding(
            "Cambridge Science Center",
            universities.get(1).getUniversityId()
        );
        if (universities.size() > 2) buildingService.addBuilding(
            "Harvard Library",
            universities.get(2).getUniversityId()
        );
        if (universities.size() > 3) buildingService.addBuilding(
            "Stanford Engineering Complex",
            universities.get(3).getUniversityId()
        );
        if (universities.size() > 4) buildingService.addBuilding(
            "MIT Media Lab",
            universities.get(4).getUniversityId()
        );
        if (universities.size() > 5) buildingService.addBuilding(
            "Yale Humanities Building",
            universities.get(5).getUniversityId()
        );
        if (universities.size() > 6) buildingService.addBuilding(
            "Princeton Economics Hall",
            universities.get(6).getUniversityId()
        );
        if (universities.size() > 7) buildingService.addBuilding(
            "Columbia Philosophy House",
            universities.get(7).getUniversityId()
        );
        if (universities.size() > 8) buildingService.addBuilding(
            "Chicago English Tower",
            universities.get(8).getUniversityId()
        );
        if (universities.size() > 9) buildingService.addBuilding(
            "Berkeley Innovation Hub",
            universities.get(9).getUniversityId()
        );

        List<Building> buildings = buildingService.getAllBuildings();
        printInitComplete("Building", buildings.size(), "buildings");
    }

    private static void initializeCourseDifficulties(CourseDifficultyService courseDifficultyService) {
        printInitStart("Course Difficulty");

        if (!courseDifficultyService.getAllCourseDifficulties().isEmpty()) {
            printInitSkip("Course Difficulty", true);
            return;
        }

        courseDifficultyService.addCourseDifficulty(new CourseDifficulty("Very Easy", 1));
        courseDifficultyService.addCourseDifficulty(new CourseDifficulty("Easy", 2));
        courseDifficultyService.addCourseDifficulty(new CourseDifficulty("Lower-Intermediate", 3));
        courseDifficultyService.addCourseDifficulty(new CourseDifficulty("Intermediate", 4));
        courseDifficultyService.addCourseDifficulty(new CourseDifficulty("Upper-Intermediate", 5));
        courseDifficultyService.addCourseDifficulty(new CourseDifficulty("Advanced", 6));
        courseDifficultyService.addCourseDifficulty(new CourseDifficulty("Upper-Advanced", 7));
        courseDifficultyService.addCourseDifficulty(new CourseDifficulty("Expert", 8));
        courseDifficultyService.addCourseDifficulty(new CourseDifficulty("Master", 9));
        courseDifficultyService.addCourseDifficulty(new CourseDifficulty("Grand Master", 10));

        List<CourseDifficulty> difficulties = courseDifficultyService.getAllCourseDifficulties();
        printInitComplete("Course Difficulty", difficulties.size(), "difficulties");
    }

    private static void initializeEnrollmentStatuses(EnrollmentStatusService enrollmentStatusService) {
        printInitStart("Enrollment Status");

        if (!enrollmentStatusService.getAllEnrollmentStatuses().isEmpty()) {
            printInitSkip("Enrollment Status", true);
            return;
        }

        enrollmentStatusService.addEnrollmentStatus(
            new com.solvd.university.model.EnrollmentStatus("Pending", "Student has applied")
        );
        enrollmentStatusService.addEnrollmentStatus(
            new com.solvd.university.model.EnrollmentStatus("Active", "Currently enrolled")
        );
        enrollmentStatusService.addEnrollmentStatus(
            new com.solvd.university.model.EnrollmentStatus("Probation", "Academic probation")
        );
        enrollmentStatusService.addEnrollmentStatus(
            new com.solvd.university.model.EnrollmentStatus("On Hold", "Temporarily suspended")
        );
        enrollmentStatusService.addEnrollmentStatus(
            new com.solvd.university.model.EnrollmentStatus("Withdrawn", "Student withdrew")
        );
        enrollmentStatusService.addEnrollmentStatus(
            new com.solvd.university.model.EnrollmentStatus("Suspended", "Administratively suspended")
        );
        enrollmentStatusService.addEnrollmentStatus(
            new com.solvd.university.model.EnrollmentStatus("Completed", "Finished program")
        );
        enrollmentStatusService.addEnrollmentStatus(
            new com.solvd.university.model.EnrollmentStatus("Graduated", "Graduated successfully")
        );
        enrollmentStatusService.addEnrollmentStatus(
            new com.solvd.university.model.EnrollmentStatus("Expelled", "Expelled from university")
        );
        enrollmentStatusService.addEnrollmentStatus(
            new com.solvd.university.model.EnrollmentStatus("Deferred", "Deferred enrollment")
        );

        List<com.solvd.university.model.EnrollmentStatus> statuses = enrollmentStatusService.getAllEnrollmentStatuses();
        printInitComplete("Enrollment Status", statuses.size(), "statuses");
    }

    private static void initializeGradeLevels(GradeLevelService gradeLevelService) {
        printInitStart("Grade Level");

        if (!gradeLevelService.getAllGradeLevels().isEmpty()) {
            printInitSkip("Grade Level", true);
            return;
        }

        gradeLevelService.addGradeLevel(new com.solvd.university.model.GradeLevel("Freshman", 1));
        gradeLevelService.addGradeLevel(new com.solvd.university.model.GradeLevel("Sophomore", 2));
        gradeLevelService.addGradeLevel(new com.solvd.university.model.GradeLevel("Junior", 3));
        gradeLevelService.addGradeLevel(new com.solvd.university.model.GradeLevel("Senior", 4));
        gradeLevelService.addGradeLevel(new com.solvd.university.model.GradeLevel("Graduate 1st Year", 5));
        gradeLevelService.addGradeLevel(new com.solvd.university.model.GradeLevel("Graduate 2nd Year", 6));
        gradeLevelService.addGradeLevel(new com.solvd.university.model.GradeLevel("PhD 1st Year", 7));
        gradeLevelService.addGradeLevel(new com.solvd.university.model.GradeLevel("PhD 2nd Year", 8));
        gradeLevelService.addGradeLevel(new com.solvd.university.model.GradeLevel("PhD 3rd Year", 9));
        gradeLevelService.addGradeLevel(new com.solvd.university.model.GradeLevel("PhD Candidate", 10));

        List<com.solvd.university.model.GradeLevel> levels = gradeLevelService.getAllGradeLevels();
        printInitComplete("Grade Level", levels.size(), "levels");
    }

    private static void initializePrograms(ProgramService programService, DepartmentService departmentService) {
        printInitStart("Program");

        if (!programService.getAllPrograms().isEmpty()) {
            printInitSkip("Program", true);
            return;
        }

        List<Department<?>> departments = departmentService.getAllDepartments();
        if (departments.isEmpty()) {
            printInitSkip("Program", false);
            System.out.println("    (Departments not found)");
            return;
        }

        Department<?> computerScience = departmentService.getDepartmentByCode("CS");
        Department<?> mathematics = departmentService.getDepartmentByCode("MATH");
        Department<?> engineering = departmentService.getDepartmentByCode("ENG");
        Department<?> business = departmentService.getDepartmentByCode("BUS");
        Department<?> arts = departmentService.getDepartmentByCode("ART");

        programService.addProgram(new Program("Bachelor of Computer Science", 4, 40000.0, computerScience));
        programService.addProgram(new Program("Master of Software Engineering", 2, 30000.0, computerScience));
        programService.addProgram(new Program("Bachelor of Information Technology", 4, 38000.0, computerScience));
        programService.addProgram(new Program("Master of Data Science", 2, 35000.0, computerScience));
        programService.addProgram(new Program("Bachelor of Mathematics", 4, 33000.0, mathematics));
        programService.addProgram(new Program("Bachelor of Applied Mathematics", 4, 35000.0, mathematics));
        programService.addProgram(new Program("Master of Statistics", 2, 28000.0, mathematics));
        programService.addProgram(new Program("Master of Pure Mathematics", 2, 26000.0, mathematics));
        programService.addProgram(new Program("Bachelor of Civil Engineering", 4, 42000.0, engineering));
        programService.addProgram(new Program("Bachelor of Mechanical Engineering", 4, 42000.0, engineering));
        programService.addProgram(new Program("Bachelor of Electrical Engineering", 4, 43000.0, engineering));
        programService.addProgram(new Program("Master of Engineering Management", 2, 38000.0, engineering));
        programService.addProgram(new Program("Bachelor of Business Administration", 4, 35000.0, business));
        programService.addProgram(new Program("Master of Business Administration", 2, 32000.0, business));
        programService.addProgram(new Program("Bachelor of Finance", 4, 37000.0, business));
        programService.addProgram(new Program("Master of Business Management", 2, 34000.0, business));
        programService.addProgram(new Program("Bachelor of Fine Arts", 4, 27000.0, arts));
        programService.addProgram(new Program("Master of Fine Arts", 2, 24000.0, arts));
        programService.addProgram(new Program("Bachelor of Art History", 4, 28000.0, arts));
        programService.addProgram(new Program("Master of Art History", 2, 25000.0, arts));

        List<Program> programs = programService.getAllPrograms();
        printInitComplete("Program", programs.size(), "programs");
    }

    private static void initializeStudents(StudentService studentService) {
        printInitStart("Student");

        if (!studentService.getAllStudents().isEmpty()) {
            printInitSkip("Student", true);
            return;
        }

        try {
            studentService.registerStudent("Alice", "Johnson", 20, "alice.johnson@university.edu");
            studentService.registerStudent("Bob", "Smith", 21, "bob.smith@university.edu");
            studentService.registerStudent("Charlie", "Davis", 22, "charlie.davis@university.edu");
            studentService.registerStudent("Diana", "Wilson", 23, "diana.wilson@university.edu");
            studentService.registerStudent("Ethan", "Martinez", 19, "ethan.martinez@university.edu");
            studentService.registerStudent("Fiona", "Garcia", 20, "fiona.garcia@university.edu");
            studentService.registerStudent("George", "Rodriguez", 21, "george.rodriguez@university.edu");
            studentService.registerStudent("Hannah", "Lee", 22, "hannah.lee@university.edu");
            studentService.registerStudent("Isaac", "Walker", 19, "isaac.walker@university.edu");
            studentService.registerStudent("Julia", "Hall", 20, "julia.hall@university.edu");

            List<Student> students = studentService.getAllStudents();
            printInitComplete("Student", students.size(), "students");
        } catch (Exception e) {
            System.out.println("    Error initializing students: " + e.getMessage());
        }
    }

    private static void initializeEnrollments(
        EnrollmentService enrollmentService,
        StudentService studentService,
        ProgramService programService
    ) {
        printInitStart("Enrollment");

        if (!enrollmentService.getAllEnrollments().isEmpty()) {
            printInitSkip("Enrollment", true);
            return;
        }

        List<Student> students = studentService.getAllStudents();
        List<Program> programs = programService.getAllPrograms();

        if (students.isEmpty()) {
            printInitSkip("Enrollment", false);
            System.out.println("    (Students not found)");
            return;
        }

        if (programs.isEmpty()) {
            printInitSkip("Enrollment", false);
            System.out.println("    (Programs not found)");
            return;
        }

        try {
            for (int i = 0; i < Math.min(10, students.size()); i++) {
                Student student = students.get(i);
                Program program = programs.get(i % programs.size());
                enrollmentService.enrollStudent(student, program);
            }

            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
            printInitComplete("Enrollment", enrollments.size(), "enrollments");
        } catch (Exception e) {
            System.out.println("    Error initializing enrollments: " + e.getMessage());
        }
    }

    private static void initializeProfessors(ProfessorService professorService, DepartmentService departmentService) {
        printInitStart("Professor");

        List<Professor> existingProfessors = professorService.getAllProfessors();
        if (!existingProfessors.isEmpty()) {
            printInitSkip("Professor", true);
            return;
        }

        Department<?> computerScience = departmentService.getDepartmentByCode("CS");
        Department<?> mathematics = departmentService.getDepartmentByCode("MATH");
        Department<?> engineering = departmentService.getDepartmentByCode("ENG");
        Department<?> business = departmentService.getDepartmentByCode("BUS");
        Department<?> arts = departmentService.getDepartmentByCode("ART");

        professorService.addProfessor(new Professor("John", "Smith", computerScience, "Professor"));
        professorService.addProfessor(new Professor("Emily", "Davis", mathematics, "Professor"));
        professorService.addProfessor(new Professor("Michael", "Brown", engineering, "Assistant Professor"));
        professorService.addProfessor(new Professor("Sarah", "Johnson", business, "Associate Professor"));
        professorService.addProfessor(new Professor("David", "Wilson", arts, "Professor"));

        List<Professor> professors = professorService.getAllProfessors();
        printInitComplete("Professor", professors.size(), "professors");
    }

    private static void initializeCourses(
        CourseService courseService,
        ProfessorService professorService,
        DepartmentService departmentService
    ) {
        printInitStart("Course");

        if (!courseService.getAllCourses().isEmpty()) {
            printInitSkip("Course", true);
            return;
        }

        List<Professor> professors = professorService.getAllProfessors();
        if (professors.isEmpty()) {
            printInitSkip("Course", false);
            System.out.println("    (Professors not found)");
            return;
        }

        Professor johnSmith = professors.get(0);
        Professor emilyDavis = professors.get(1);
        Professor michaelBrown = professors.get(2);
        Professor sarahJohnson = professors.get(3);
        Professor davidWilson = professors.get(4);

        ComputerScienceDepartment computerScience = (ComputerScienceDepartment) departmentService.getDepartmentByCode(
            "CS"
        );
        MathematicsDepartment mathematics = (MathematicsDepartment) departmentService.getDepartmentByCode("MATH");
        EngineeringDepartment engineering = (EngineeringDepartment) departmentService.getDepartmentByCode("ENG");
        BusinessDepartment business = (BusinessDepartment) departmentService.getDepartmentByCode("BUS");
        ArtsDepartment arts = (ArtsDepartment) departmentService.getDepartmentByCode("ART");

        Course<String, ComputerScienceDepartment> introductionToProgramming = Course
            .<String, ComputerScienceDepartment>builder()
            .number(101)
            .name("Introduction to Programming")
            .creditHours(3)
            .professor(johnSmith)
            .department(computerScience)
            .difficulty(CourseDifficulty.INTRODUCTORY)
            .build();
        Course<String, ComputerScienceDepartment> dataStructures = Course
            .<String, ComputerScienceDepartment>builder()
            .number(201)
            .name("Data Structures")
            .creditHours(4)
            .professor(johnSmith)
            .department(computerScience)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();
        Course<String, ComputerScienceDepartment> algorithms = Course
            .<String, ComputerScienceDepartment>builder()
            .number(301)
            .name("Algorithms")
            .creditHours(4)
            .professor(johnSmith)
            .department(computerScience)
            .difficulty(CourseDifficulty.ADVANCED)
            .build();
        Course<String, ComputerScienceDepartment> webDevelopment = Course
            .<String, ComputerScienceDepartment>builder()
            .number(250)
            .name("Web Development")
            .creditHours(3)
            .professor(johnSmith)
            .department(computerScience)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();
        Course<String, ComputerScienceDepartment> databaseSystems = Course
            .<String, ComputerScienceDepartment>builder()
            .number(310)
            .name("Database Systems")
            .creditHours(4)
            .professor(johnSmith)
            .department(computerScience)
            .difficulty(CourseDifficulty.ADVANCED)
            .build();

        Course<String, MathematicsDepartment> calculus = Course
            .<String, MathematicsDepartment>builder()
            .number(101)
            .name("Calculus I")
            .creditHours(4)
            .professor(emilyDavis)
            .department(mathematics)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();
        Course<String, MathematicsDepartment> linearAlgebra = Course
            .<String, MathematicsDepartment>builder()
            .number(201)
            .name("Linear Algebra")
            .creditHours(4)
            .professor(emilyDavis)
            .department(mathematics)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();
        Course<String, MathematicsDepartment> statistics = Course
            .<String, MathematicsDepartment>builder()
            .number(220)
            .name("Statistics")
            .creditHours(3)
            .professor(emilyDavis)
            .department(mathematics)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();
        Course<String, MathematicsDepartment> discreteMath = Course
            .<String, MathematicsDepartment>builder()
            .number(150)
            .name("Discrete Mathematics")
            .creditHours(3)
            .professor(emilyDavis)
            .department(mathematics)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();
        Course<String, MathematicsDepartment> calculusII = Course
            .<String, MathematicsDepartment>builder()
            .number(102)
            .name("Calculus II")
            .creditHours(4)
            .professor(emilyDavis)
            .department(mathematics)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();

        Course<String, EngineeringDepartment> mechanicsI = Course
            .<String, EngineeringDepartment>builder()
            .number(101)
            .name("Engineering Mechanics I")
            .creditHours(4)
            .professor(michaelBrown)
            .department(engineering)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();
        Course<String, EngineeringDepartment> thermodynamics = Course
            .<String, EngineeringDepartment>builder()
            .number(210)
            .name("Thermodynamics")
            .creditHours(4)
            .professor(michaelBrown)
            .department(engineering)
            .difficulty(CourseDifficulty.ADVANCED)
            .build();
        Course<String, EngineeringDepartment> circuitAnalysis = Course
            .<String, EngineeringDepartment>builder()
            .number(220)
            .name("Circuit Analysis")
            .creditHours(4)
            .professor(michaelBrown)
            .department(engineering)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();
        Course<String, EngineeringDepartment> materialsScience = Course
            .<String, EngineeringDepartment>builder()
            .number(230)
            .name("Materials Science")
            .creditHours(3)
            .professor(michaelBrown)
            .department(engineering)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();
        Course<String, EngineeringDepartment> fluidMechanics = Course
            .<String, EngineeringDepartment>builder()
            .number(310)
            .name("Fluid Mechanics")
            .creditHours(4)
            .professor(michaelBrown)
            .department(engineering)
            .difficulty(CourseDifficulty.ADVANCED)
            .build();

        Course<String, BusinessDepartment> accounting = Course
            .<String, BusinessDepartment>builder()
            .number(101)
            .name("Financial Accounting")
            .creditHours(3)
            .professor(sarahJohnson)
            .department(business)
            .difficulty(CourseDifficulty.INTRODUCTORY)
            .build();
        Course<String, BusinessDepartment> marketing = Course
            .<String, BusinessDepartment>builder()
            .number(210)
            .name("Marketing Fundamentals")
            .creditHours(3)
            .professor(sarahJohnson)
            .department(business)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();
        Course<String, BusinessDepartment> finance = Course
            .<String, BusinessDepartment>builder()
            .number(220)
            .name("Corporate Finance")
            .creditHours(4)
            .professor(sarahJohnson)
            .department(business)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();
        Course<String, BusinessDepartment> management = Course
            .<String, BusinessDepartment>builder()
            .number(230)
            .name("Management Principles")
            .creditHours(3)
            .professor(sarahJohnson)
            .department(business)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();
        Course<String, BusinessDepartment> businessEthics = Course
            .<String, BusinessDepartment>builder()
            .number(310)
            .name("Business Ethics")
            .creditHours(3)
            .professor(sarahJohnson)
            .department(business)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();

        Course<String, ArtsDepartment> artHistory = Course
            .<String, ArtsDepartment>builder()
            .number(101)
            .name("Art History I")
            .creditHours(3)
            .professor(davidWilson)
            .department(arts)
            .difficulty(CourseDifficulty.INTRODUCTORY)
            .build();
        Course<String, ArtsDepartment> drawing = Course
            .<String, ArtsDepartment>builder()
            .number(110)
            .name("Drawing Fundamentals")
            .creditHours(3)
            .professor(davidWilson)
            .department(arts)
            .difficulty(CourseDifficulty.INTRODUCTORY)
            .build();
        Course<String, ArtsDepartment> painting = Course
            .<String, ArtsDepartment>builder()
            .number(210)
            .name("Painting Techniques")
            .creditHours(3)
            .professor(davidWilson)
            .department(arts)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();
        Course<String, ArtsDepartment> sculpture = Course
            .<String, ArtsDepartment>builder()
            .number(220)
            .name("Sculpture Studio")
            .creditHours(4)
            .professor(davidWilson)
            .department(arts)
            .difficulty(CourseDifficulty.INTERMEDIATE)
            .build();
        Course<String, ArtsDepartment> digitalArt = Course
            .<String, ArtsDepartment>builder()
            .number(310)
            .name("Digital Art and Design")
            .creditHours(3)
            .professor(davidWilson)
            .department(arts)
            .difficulty(CourseDifficulty.ADVANCED)
            .build();

        johnSmith.assignCourse(introductionToProgramming);
        johnSmith.assignCourse(dataStructures);
        johnSmith.assignCourse(algorithms);
        johnSmith.assignCourse(webDevelopment);
        johnSmith.assignCourse(databaseSystems);

        emilyDavis.assignCourse(calculus);
        emilyDavis.assignCourse(linearAlgebra);
        emilyDavis.assignCourse(statistics);
        emilyDavis.assignCourse(discreteMath);
        emilyDavis.assignCourse(calculusII);

        michaelBrown.assignCourse(mechanicsI);
        michaelBrown.assignCourse(thermodynamics);
        michaelBrown.assignCourse(circuitAnalysis);
        michaelBrown.assignCourse(materialsScience);
        michaelBrown.assignCourse(fluidMechanics);

        sarahJohnson.assignCourse(accounting);
        sarahJohnson.assignCourse(marketing);
        sarahJohnson.assignCourse(finance);
        sarahJohnson.assignCourse(management);
        sarahJohnson.assignCourse(businessEthics);

        davidWilson.assignCourse(artHistory);
        davidWilson.assignCourse(drawing);
        davidWilson.assignCourse(painting);
        davidWilson.assignCourse(sculpture);
        davidWilson.assignCourse(digitalArt);

        courseService.addCourse(introductionToProgramming);
        courseService.addCourse(dataStructures);
        courseService.addCourse(algorithms);
        courseService.addCourse(webDevelopment);
        courseService.addCourse(databaseSystems);

        courseService.addCourse(calculus);
        courseService.addCourse(linearAlgebra);
        courseService.addCourse(statistics);
        courseService.addCourse(discreteMath);
        courseService.addCourse(calculusII);

        courseService.addCourse(mechanicsI);
        courseService.addCourse(thermodynamics);
        courseService.addCourse(circuitAnalysis);
        courseService.addCourse(materialsScience);
        courseService.addCourse(fluidMechanics);

        courseService.addCourse(accounting);
        courseService.addCourse(marketing);
        courseService.addCourse(finance);
        courseService.addCourse(management);
        courseService.addCourse(businessEthics);

        courseService.addCourse(artHistory);
        courseService.addCourse(drawing);
        courseService.addCourse(painting);
        courseService.addCourse(sculpture);
        courseService.addCourse(digitalArt);

        List<Course<?, ?>> courses = courseService.getAllCourses();
        printInitComplete("Course", courses.size(), "courses");
    }

    private static void initializeClassrooms(ClassroomService classroomService, BuildingService buildingService) {
        printInitStart("Classroom");

        if (!classroomService.getAllClassrooms().isEmpty()) {
            printInitSkip("Classroom", true);
            return;
        }

        Building oxfordMainHall = buildingService.getBuildingByName("Oxford Main Hall");
        Building cambridgeScienceCenter = buildingService.getBuildingByName("Cambridge Science Center");
        Building stanfordEngineering = buildingService.getBuildingByName("Stanford Engineering Complex");
        Building mitMediaLab = buildingService.getBuildingByName("MIT Media Lab");

        classroomService.addClassroom(new Classroom("101", oxfordMainHall, 50, "Lecture Hall"));
        classroomService.addClassroom(new Classroom("205", cambridgeScienceCenter, 30, "Computer Lab"));
        classroomService.addClassroom(new Classroom("301", stanfordEngineering, 25, "Laboratory"));
        classroomService.addClassroom(new Classroom("150", mitMediaLab, 40, "Workshop"));

        List<Classroom> classrooms = classroomService.getAllClassrooms();
        printInitComplete("Classroom", classrooms.size(), "classrooms");
    }

    private static void initializeStudentGrades(StudentGradeService studentGradeService) {
        printInitStart("Student Grade");

        if (!studentGradeService.getAllGrades().isEmpty()) {
            printInitSkip("Student Grade", true);
            return;
        }

        List<StudentGrade> grades = studentGradeService.getAllGrades();
        printInitComplete("Student Grade", grades.size(), "student grades");
    }

    private static void initializeCourseGrades(CourseGradeService courseGradeService) {
        printInitStart("Course Grade");

        if (!courseGradeService.getAllGrades().isEmpty()) {
            printInitSkip("Course Grade", true);
            return;
        }

        List<CourseGrade> grades = courseGradeService.getAllGrades();
        printInitComplete("Course Grade", grades.size(), "course grades");
    }

    private static void initializePersons(PersonService personService) {
        printInitStart("Person");

        if (!personService.getAllPersons().isEmpty()) {
            printInitSkip("Person", true);
            return;
        }

        List<Person> persons = personService.getAllPersons();
        printInitComplete("Person", persons.size(), "persons");
    }

    private static void initializeStaff(StaffService staffService) {
        printInitStart("Staff");

        if (!staffService.getAllStaff().isEmpty()) {
            printInitSkip("Staff", true);
            return;
        }

        List<Staff> staff = staffService.getAllStaff();
        printInitComplete("Staff", staff.size(), "staff");
    }

    private static void printInitStart(String tableName) {
        System.out.println("\n" + "-".repeat(60));
        System.out.println(">>> Initializing " + tableName + "...");
    }

    private static void printInitSkip(String tableName, boolean alreadyExists) {
        if (alreadyExists) {
            System.out.println("    ✓ " + tableName + " already exists. Skipping initialization.");
        } else {
            System.out.println("    • " + tableName + " does not need initialization. Skipping.");
        }
        System.out.println("-".repeat(60));
    }

    private static void printInitComplete(String tableName, int count, String itemType) {
        System.out.println("    ✓ Initialization complete!");
        System.out.println("    → " + count + " " + itemType);
        System.out.println("-".repeat(50));
    }
}
