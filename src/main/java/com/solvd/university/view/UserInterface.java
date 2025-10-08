package com.solvd.university.view;

import com.solvd.university.model.CourseFormatter;
import com.solvd.university.model.Department;
import com.solvd.university.model.Enrollment;
import com.solvd.university.model.EnrollmentStatus;
import com.solvd.university.model.Grade;
import com.solvd.university.model.GradeValidator;
import com.solvd.university.model.Program;
import com.solvd.university.model.Student;
import com.solvd.university.model.StudentFilter;
import com.solvd.university.model.exception.AlreadyEnrolledException;
import com.solvd.university.model.exception.DuplicateRegistrationException;
import com.solvd.university.model.exception.InvalidPaymentException;
import com.solvd.university.model.exception.StudentNotEnrolledException;
import com.solvd.university.model.exception.StudentNotFoundException;
import com.solvd.university.service.interfaces.BuildingService;
import com.solvd.university.service.interfaces.ClassroomService;
import com.solvd.university.service.interfaces.CourseGradeService;
import com.solvd.university.service.interfaces.CourseService;
import com.solvd.university.service.interfaces.DepartmentService;
import com.solvd.university.service.interfaces.EnrollmentService;
import com.solvd.university.service.interfaces.ProfessorService;
import com.solvd.university.service.interfaces.ProgramService;
import com.solvd.university.service.interfaces.StudentGradeService;
import com.solvd.university.service.interfaces.StudentService;
import com.solvd.university.service.interfaces.UniversityService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserInterface {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Scanner scanner;
    private final String universityName;
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;
    private final ProgramService programService;
    private final ProfessorService professorService;
    private final CourseService courseService;
    private final ClassroomService classroomService;

    private static final GradeValidator STANDARD_GRADE_VALIDATOR = grade -> grade >= 0.0 && grade <= 100.0;
    private static final GradeValidator PASSING_GRADE_VALIDATOR = grade -> grade >= 60.0 && grade <= 100.0;

    private static final StudentFilter ACTIVE_STUDENTS = Student::isEnrolled;
    private static final StudentFilter HONOR_STUDENTS = student -> student.calculateAverageGrade() >= 90.0;
    private static final StudentFilter STUDENTS_WITH_DEBT = student -> student.getOutstandingBalance() > 0;

    private static final CourseFormatter DETAILED_COURSE_FORMAT = course ->
        String.format(
            "%s - %s (%d credits) - %s [%s]",
            course.getCourseCode(),
            course.getCourseName(),
            course.getCreditHours(),
            course.getProfessor().getFullName(),
            course.getDifficulty().getDisplayName()
        );

    public UserInterface(
        Scanner scanner,
        String universityName,
        UniversityService universityService,
        DepartmentService departmentService,
        BuildingService buildingService,
        StudentService studentService,
        EnrollmentService enrollmentService,
        ProgramService programService,
        ProfessorService professorService,
        CourseService courseService,
        ClassroomService classroomService,
        StudentGradeService studentGradeService,
        CourseGradeService courseGradeService
    ) {
        this.scanner = scanner;
        this.universityName = universityName;
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
        this.programService = programService;
        this.professorService = professorService;
        this.courseService = courseService;
        this.classroomService = classroomService;
    }

    public void start() {
        while (true) {
            entryMessage();

            int option = getIntInput("Select option: ");

            switch (option) {
                case 1 -> handleStudentRegistration();
                case 2 -> handleStudentLogin();
                case 3 -> showAvailableProgramsAsGuest();
                case 4 -> showProfessorsAndCourses();
                case 5 -> {
                    LOGGER.info("Thanks for visiting. Come back soon!");
                    return;
                }
                default -> LOGGER.info("Invalid option. Please try again.");
            }
        }
    }

    private void handleStudentRegistration() {
        LOGGER.info("=== Student Registration ===");
        LOGGER.info("");
        LOGGER.info("Enter your details:");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        int age = getIntInput("Age: ");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        LOGGER.info("");

        try {
            registerStudent(firstName, lastName, age, email);
        } catch (DuplicateRegistrationException e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("");
        }
    }

    private void registerStudent(String firstName, String lastName, int age, String email)
        throws DuplicateRegistrationException {
        Student student = studentService.registerStudent(firstName, lastName, age, email);

        LOGGER.info("Registration successful! Now you can enroll in a program.");
        LOGGER.info("Use the following credentials to log in: ");
        LOGGER.info("Email: " + student.getEmail());
        LOGGER.info("Student Number: " + student.getStudentNumber());
        LOGGER.info("");
    }

    private void handleStudentLogin() {
        LOGGER.info("=== Student Login ===");
        LOGGER.info("");

        System.out.print("Email: ");
        String email = scanner.nextLine();

        int studentId = getIntInput("Student Number (6 digits): ");

        try {
            Student student = authenticateStudent(email, studentId);
            loginDashboard(student);
        } catch (StudentNotFoundException e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("");
        }
    }

    private Student authenticateStudent(String email, int studentId) throws StudentNotFoundException {
        return studentService.authenticateStudent(email, studentId);
    }

    private void entryMessage() {
        LOGGER.info(String.format("=== %s University Enrollment System ===", universityName));
        LOGGER.info("");

        LOGGER.info("1. Register as new student.");
        LOGGER.info("2. Log in as existing student.");
        LOGGER.info("3. Browse programs as guest");
        LOGGER.info("4. View enrollment statistics, popular courses, professors, and others");
        LOGGER.info("5. Exit");
    }

    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                LOGGER.info("Please enter a valid number.");
                LOGGER.info("");
            }
        }
    }

    private void loginDashboard(Student student) {
        LOGGER.info("Login successful!");
        LOGGER.info("");

        LOGGER.info(String.format("Welcome, %s!", student.getFullName()));
        LOGGER.info(
            String.format(
                "Academic Level: %s (Year %d)",
                student.getGradeLevel().getDisplayName(),
                student.getGradeLevel().getYear()
            )
        );
        LOGGER.info("");

        String statusMessage;
        if (student.isEnrolled() && student.getEnrolledProgram() != null) {
            statusMessage = String.format(
                "You are enrolled in %s (Status: %s)",
                student.getEnrolledProgram().getName(),
                student.getEnrollmentStatus().getDisplayName()
            );
        } else if (student.getEnrollmentStatus().equals(EnrollmentStatus.ENROLLED)) {
            statusMessage =
                "Status: " + student.getEnrollmentStatus().getDisplayName() + " (Program enrollment being processed)";
        } else {
            statusMessage = "Status: " + student.getEnrollmentStatus().getDisplayName();
        }

        LOGGER.info("Current Status: " + statusMessage);

        while (true) {
            LOGGER.info("1. View my enrollment");
            LOGGER.info("2. Enroll in a program");
            LOGGER.info("3. Make a payment");
            LOGGER.info("4. View my grades");
            LOGGER.info("5. Log out");
            LOGGER.info("");

            int option = getIntInput("Select option: ");

            switch (option) {
                case 1 -> {
                    try {
                        viewEnrollmentDetails(student);
                    } catch (StudentNotEnrolledException e) {
                        LOGGER.error(e.getMessage());
                        LOGGER.info("");
                    }
                }
                case 2 -> {
                    try {
                        handleEnrollment(student);
                    } catch (AlreadyEnrolledException e) {
                        LOGGER.error(e.getMessage());
                        LOGGER.info("");
                    }
                }
                case 3 -> {
                    try {
                        handlePayment(student);
                    } catch (StudentNotEnrolledException | InvalidPaymentException e) {
                        LOGGER.error(e.getMessage());
                        LOGGER.info("");
                    }
                }
                case 4 -> {
                    viewStudentGrades(student);
                }
                case 5 -> {
                    return;
                }
                default -> LOGGER.info("Invalid option. Please try again.");
            }
        }
    }

    private void viewStudentGrades(Student student) {
        LOGGER.info("=== My Academic Record ===");
        LOGGER.info("");

        List<Grade<Double>> grades = student.getGrades();

        if (grades.isEmpty()) {
            LOGGER.info("No grades recorded yet.");
            LOGGER.info("");
            return;
        }

        LOGGER.info(String.format("Overall Average: %.2f", student.calculateAverageGrade()));
        LOGGER.info(String.format("Total Courses: %d", grades.size()));

        boolean isPassing = PASSING_GRADE_VALIDATOR.isValid(student.calculateAverageGrade());
        LOGGER.info(String.format("Academic Standing: %s", isPassing ? "Good Standing" : "Academic Probation"));
        LOGGER.info("");

        Map<String, List<Grade<Double>>> gradesBySemester = grades
            .stream()
            .collect(Collectors.groupingBy(Grade::semester));

        gradesBySemester
            .entrySet()
            .forEach(entry -> {
                String semester = entry.getKey();
                List<Grade<Double>> semesterGrades = entry.getValue();

                LOGGER.info(String.format("=== %s ===", semester));
                double semesterAvg = student.calculateSemesterAverage(semester);
                LOGGER.info(String.format("Semester Average: %.2f", semesterAvg));

                boolean semesterPassing = PASSING_GRADE_VALIDATOR.isValid(semesterAvg);
                LOGGER.info(
                    String.format("Semester Status: %s", semesterPassing ? "Satisfactory" : "Needs Improvement")
                );
                LOGGER.info("");

                semesterGrades.forEach(grade -> {
                    String letterGrade = convertToLetterGrade(grade.value());
                    boolean gradeValid = STANDARD_GRADE_VALIDATOR.isValid(grade.value());
                    String validationMark = gradeValid ? "✓" : "⚠";
                    LOGGER.info(
                        String.format(
                            "  %-30s | %.1f (%s) %s",
                            grade.subject(),
                            grade.value(),
                            letterGrade,
                            validationMark
                        )
                    );
                });
                LOGGER.info("");
            });

        long aGrades = grades
            .stream()
            .filter(g -> g.value() >= 90)
            .count();
        long bGrades = grades
            .stream()
            .filter(g -> g.value() >= 80 && g.value() < 90)
            .count();
        long cGrades = grades
            .stream()
            .filter(g -> g.value() >= 70 && g.value() < 80)
            .count();
        long dGrades = grades
            .stream()
            .filter(g -> g.value() >= 60 && g.value() < 70)
            .count();
        long fGrades = grades
            .stream()
            .filter(g -> g.value() < 60)
            .count();

        LOGGER.info("=== Grade Distribution ===");
        LOGGER.info(
            String.format(
                "A's: %d | B's: %d | C's: %d | D's: %d | F's: %d",
                aGrades,
                bGrades,
                cGrades,
                dGrades,
                fGrades
            )
        );
        LOGGER.info("");
    }

    private String convertToLetterGrade(double numericGrade) {
        if (numericGrade >= 97) return "A+";
        else if (numericGrade >= 93) return "A";
        else if (numericGrade >= 90) return "A-";
        else if (numericGrade >= 87) return "B+";
        else if (numericGrade >= 83) return "B";
        else if (numericGrade >= 80) return "B-";
        else if (numericGrade >= 77) return "C+";
        else if (numericGrade >= 73) return "C";
        else if (numericGrade >= 70) return "C-";
        else if (numericGrade >= 67) return "D+";
        else if (numericGrade >= 63) return "D";
        else if (numericGrade >= 60) return "D-";
        else return "F";
    }

    private void viewEnrollmentDetails(Student student) throws StudentNotEnrolledException {
        if (!student.isEnrolled()) {
            throw new StudentNotEnrolledException(
                "Student must be enrolled in a program to view enrollment details. Please enroll in a program first."
            );
        }

        Enrollment studentEnrollment = null;
        try {
            studentEnrollment = enrollmentService.getEnrollmentByStudent(student);
        } catch (StudentNotEnrolledException e) {}

        LOGGER.info("=== Enrollment Details ===");
        LOGGER.info("");

        LOGGER.info("Student: " + student.getFullName());
        LOGGER.info(
            "Academic Level: " +
                student.getGradeLevel().getDisplayName() +
                " (Year " +
                student.getGradeLevel().getYear() +
                ")"
        );
        LOGGER.info("Program: " + student.getEnrolledProgram().getName());
        LOGGER.info("Department: " + student.getEnrolledProgram().getDepartment().getName());
        LOGGER.info("Program Price: " + student.getEnrolledProgram().getPrice());
        LOGGER.info("Outstanding Balance: " + student.getOutstandingBalanceFormatted());
        LOGGER.info("Enrollment Status: " + student.getEnrollmentStatus().getDisplayName());
        LOGGER.info("Status Description: " + student.getEnrollmentStatus().getDescription());

        if (studentEnrollment != null) {
            LOGGER.info("Enrollment Date: " + studentEnrollment.enrollmentDate());
        }
        LOGGER.info("");
    }

    private void handleEnrollment(Student student) throws AlreadyEnrolledException {
        if (student.isEnrolled()) {
            throw new AlreadyEnrolledException(
                "Student is already enrolled in '" +
                    student.getEnrolledProgram().getName() +
                    "'. Cannot enroll in multiple programs simultaneously."
            );
        }
        showAvailablePrograms(student);
    }

    private void handlePayment(Student student) throws StudentNotEnrolledException, InvalidPaymentException {
        if (!student.isEnrolled()) {
            throw new StudentNotEnrolledException(
                "Student must be enrolled in a program to make a payment. Please enroll in a program first."
            );
        }

        if (student.getOutstandingBalance() <= 0.0) {
            LOGGER.info("There is no outstanding balance to pay.");
            LOGGER.info("");
            return;
        }

        double payment = getIntInput("Enter payment amount: ");

        enrollmentService.processPayment(student, payment);
        LOGGER.info("Payment recorded. New balance: " + student.getOutstandingBalanceFormatted());
        if (student.getOutstandingBalance() == 0.0) {
            LOGGER.info("Congratulations! You have fully paid your program. Best of luck with your studies!");
        }
        LOGGER.info("");
    }

    private void showAvailablePrograms(Student student) {
        Map<Department<?>, List<Program>> availablePrograms = programService
            .getAllPrograms()
            .stream()
            .filter(program -> program.getDepartment() != null)
            .collect(Collectors.groupingBy(Program::getDepartment));

        List<Program> programsList = new ArrayList<>();
        AtomicInteger programsOrder = new AtomicInteger(1);

        LOGGER.info("=== Available Programs ===");

        availablePrograms
            .entrySet()
            .forEach(entry -> {
                LOGGER.info(
                    "Department: " + entry.getKey().getName() + " (" + entry.getKey().getDepartmentCode() + ")"
                );
                entry
                    .getValue()
                    .forEach(program -> {
                        LOGGER.info(programsOrder.get() + ". " + program.getName());
                        programsList.add(program);
                        programsOrder.incrementAndGet();
                    });
                LOGGER.info("");
            });

        while (true) {
            LOGGER.info("Enter zero (0) to go back or...");
            int option = getIntInput("Select the program you want to enroll in: ");

            if (option == 0) {
                break;
            }

            if (option >= 1 && option <= programsList.size()) {
                Program selectedProgram = programsList.get(option - 1);

                try {
                    Enrollment enrollment = enrollmentService.enrollStudent(student, selectedProgram);

                    LOGGER.info("Congrats! You have enrolled in " + selectedProgram.getName());
                    LOGGER.info("Enrollment Date: " + enrollment.enrollmentDate());
                    LOGGER.info("Outstanding Balance: " + student.getOutstandingBalanceFormatted());
                    LOGGER.info("");
                    break;
                } catch (AlreadyEnrolledException e) {
                    LOGGER.error(e.getMessage());
                }
            } else {
                LOGGER.info("Invalid selection. Please try again.");
            }
        }
    }

    private void showAvailableProgramsAsGuest() {
        Map<Department<?>, List<Program>> availablePrograms = programService
            .getAllPrograms()
            .stream()
            .collect(Collectors.groupingBy(Program::getDepartment));

        LOGGER.info("=== Available Programs ===");
        LOGGER.info("");

        availablePrograms
            .entrySet()
            .forEach(entry -> {
                LOGGER.info(
                    "Department: " + entry.getKey().getName() + " (" + entry.getKey().getDepartmentCode() + ")"
                );
                entry
                    .getValue()
                    .forEach(program -> {
                        LOGGER.info(program.toString());
                    });
                LOGGER.info("");
            });

        getIntInput("Enter zero (0) to go back: ");
    }

    private void showProfessorsAndCourses() {
        LOGGER.info("=== Student Enrollment Statistics ===");

        List<Student> activeStudents = studentService.getStudents(ACTIVE_STUDENTS);
        List<Student> honorStudents = studentService.getStudents(HONOR_STUDENTS);
        List<Student> studentsWithDebt = studentService.getStudents(STUDENTS_WITH_DEBT);

        LOGGER.info(String.format("Total Students: %d", studentService.getAllStudents().size()));
        LOGGER.info(String.format("Active Students: %d", activeStudents.size()));
        LOGGER.info(String.format("Honor Students (≥90%% avg): %d", honorStudents.size()));
        LOGGER.info(String.format("Students with Outstanding Balance: %d", studentsWithDebt.size()));
        LOGGER.info("");

        if (!honorStudents.isEmpty()) {
            LOGGER.info("Honor Students:");
            honorStudents
                .stream()
                .limit(5)
                .forEach(student ->
                    LOGGER.info(
                        String.format("   - %s (%.1f%% avg)", student.getFullName(), student.calculateAverageGrade())
                    )
                );
            LOGGER.info("");
        }

        Map<EnrollmentStatus, List<Student>> studentsByStatus = studentService
            .getAllStudents()
            .stream()
            .collect(Collectors.groupingBy(Student::getEnrollmentStatus));

        studentsByStatus
            .entrySet()
            .forEach(entry -> {
                EnrollmentStatus status = entry.getKey();
                List<Student> studentsWithStatus = entry.getValue();
                LOGGER.info(
                    String.format(
                        "%s Students: %d (%s)",
                        status.getDisplayName(),
                        studentsWithStatus.size(),
                        status.getDescription()
                    )
                );

                if (!studentsWithStatus.isEmpty() && status.equals(EnrollmentStatus.ENROLLED)) {
                    LOGGER.info("  Recently Enrolled Students:");
                    studentsWithStatus
                        .stream()
                        .filter(Student::isEnrolled)
                        .limit(3)
                        .forEach(student ->
                            LOGGER.info(
                                "   - " + student.getFullName() + " in " + student.getEnrolledProgram().getName()
                            )
                        );
                }
            });

        LOGGER.info("");
        LOGGER.info("=== Professors ===");

        professorService
            .getAllProfessors()
            .forEach(professor -> {
                LOGGER.info(professor.toString());
                if (!professor.getAssignedCourses().isEmpty()) {
                    LOGGER.info("  Assigned Courses:");
                    professor
                        .getAssignedCourses()
                        .forEach(course -> {
                            LOGGER.info(
                                String.format(
                                    "   - %s (%s) - %s",
                                    course.getCourseName(),
                                    course.getCourseCode(),
                                    course.getDifficulty().getDisplayName()
                                )
                            );
                        });
                } else {
                    LOGGER.info("  No courses assigned");
                }
                LOGGER.info("");
            });

        LOGGER.info("");
        LOGGER.info("=== Courses ===");

        List<String> detailedCourseList = courseService.formatCourses(DETAILED_COURSE_FORMAT);
        detailedCourseList
            .stream()
            .limit(10)
            .forEach(course -> LOGGER.info("  " + course));

        LOGGER.info("");
        LOGGER.info("=== Classrooms ===");

        classroomService
            .getAllClassrooms()
            .stream()
            .limit(10)
            .forEach(classroom -> {
                LOGGER.info(classroom.toString());
            });

        LOGGER.info("");
        getIntInput("Enter zero (0) to go back: ");
    }
}
