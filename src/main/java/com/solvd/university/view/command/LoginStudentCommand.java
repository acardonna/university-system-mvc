package com.solvd.university.view.command;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.model.Student;
import com.solvd.university.model.exception.StudentNotFoundException;
import com.solvd.university.service.interfaces.StudentService;
import com.solvd.university.view.StudentDashboard;

public class LoginStudentCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(LoginStudentCommand.class);

    private final StudentService studentService;
    private final Scanner scanner;
    private final StudentDashboard studentDashboard;

    public LoginStudentCommand(StudentService studentService, Scanner scanner, StudentDashboard studentDashboard) {
        this.studentService = studentService;
        this.scanner = scanner;
        this.studentDashboard = studentDashboard;
    }

    @Override
    public void execute() {
        LOGGER.info("=== Student Login ===");
        LOGGER.info("");

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Student Number (6 digits): ");
        int studentId;
        try {
            studentId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid student number");
            LOGGER.info("");
            return;
        }

        try {
            Student student = studentService.authenticateStudent(email, studentId);
            studentDashboard.displayDashboard(student);
        } catch (StudentNotFoundException e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("");
        }
    }

    @Override
    public String getDescription() {
        return "Log in as existing student";
    }
}
