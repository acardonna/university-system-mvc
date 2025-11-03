package com.solvd.university.view.command;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.model.Student;
import com.solvd.university.model.exception.DuplicateRegistrationException;
import com.solvd.university.service.interfaces.StudentService;

public class RegisterStudentCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(RegisterStudentCommand.class);

    private final StudentService studentService;
    private final Scanner scanner;

    public RegisterStudentCommand(StudentService studentService, Scanner scanner) {
        this.studentService = studentService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        LOGGER.info("=== Student Registration ===");
        LOGGER.info("");
        LOGGER.info("Enter your details:");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Age: ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid age entered");
            return;
        }
        System.out.print("Email: ");
        String email = scanner.nextLine();
        LOGGER.info("");

        try {
            Student student = studentService.registerStudent(firstName, lastName, age, email);
            LOGGER.info("Registration successful! Now you can enroll in a program.");
            LOGGER.info("Use the following credentials to log in: ");
            LOGGER.info("Email: " + student.getEmail());
            LOGGER.info("Student Number: " + student.getStudentNumber());
            LOGGER.info("");
        } catch (DuplicateRegistrationException e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("");
        }
    }

    @Override
    public String getDescription() {
        return "Register as new student";
    }
}
