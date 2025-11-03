package com.solvd.university.view;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.model.*;
import com.solvd.university.model.exception.AlreadyEnrolledException;
import com.solvd.university.model.exception.InvalidPaymentException;
import com.solvd.university.model.exception.StudentNotEnrolledException;
import com.solvd.university.service.interfaces.EnrollmentService;
import com.solvd.university.service.interfaces.ProgramService;

public class StudentDashboard {
    private static final Logger LOGGER = LogManager.getLogger(StudentDashboard.class);

    private final Scanner scanner;
    private final EnrollmentService enrollmentService;
    private final ProgramService programService;

    private static final GradeValidator STANDARD_GRADE_VALIDATOR = grade -> grade >= 0.0 && grade <= 100.0;
    private static final GradeValidator PASSING_GRADE_VALIDATOR = grade -> grade >= 60.0 && grade <= 100.0;

    public StudentDashboard(Scanner scanner, EnrollmentService enrollmentService, ProgramService programService) {
        this.scanner = scanner;
        this.enrollmentService = enrollmentService;
        this.programService = programService;
    }

    public void displayDashboard(Student student) {
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
        Map<Object, List<Program>> availablePrograms = programService
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
                Object dept = entry.getKey();
                LOGGER.info(
                    "Department: " + dept.getClass().getSimpleName()
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
}
