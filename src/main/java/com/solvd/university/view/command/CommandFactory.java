package com.solvd.university.view.command;

import java.util.Scanner;

import com.solvd.university.service.factory.ServiceFactory;
import com.solvd.university.service.interfaces.*;
import com.solvd.university.view.StudentDashboard;

public class CommandFactory {

    private CommandFactory() {}

    public static Command createRegisterStudentCommand(StudentService studentService, Scanner scanner) {
        return new RegisterStudentCommand(studentService, scanner);
    }

    public static Command createLoginStudentCommand(StudentService studentService,
                                                      EnrollmentService enrollmentService,
                                                      ProgramService programService,
                                                      Scanner scanner) {
        StudentDashboard dashboard = new StudentDashboard(scanner, enrollmentService, programService);
        return new LoginStudentCommand(studentService, scanner, dashboard);
    }

    public static Command createBrowseProgramsCommand(ProgramService programService) {
        return new BrowseProgramsCommand(programService);
    }

    public static Command createViewStatisticsCommand(DepartmentService departmentService,
                                                       ProfessorService professorService,
                                                       CourseService courseService) {
        return new ViewStatisticsCommand(departmentService, professorService, courseService);
    }

    public static Command createExitCommand() {
        return new ExitCommand();
    }

    public static CommandInvoker createGuestCommandInvoker(Scanner scanner) {
        CommandInvoker invoker = new CommandInvoker();

        ProgramService programService = ServiceFactory.create(ProgramService.class);
        DepartmentService departmentService = ServiceFactory.create(DepartmentService.class);
        ProfessorService professorService = ServiceFactory.create(ProfessorService.class);
        CourseService courseService = ServiceFactory.create(CourseService.class);
        StudentService studentService = ServiceFactory.create(StudentService.class);
        EnrollmentService enrollmentService = ServiceFactory.create(EnrollmentService.class);

        invoker.registerCommand(1, createRegisterStudentCommand(studentService, scanner));
        invoker.registerCommand(2, createLoginStudentCommand(studentService, enrollmentService, programService, scanner));
        invoker.registerCommand(3, createBrowseProgramsCommand(programService));
        invoker.registerCommand(4, createViewStatisticsCommand(departmentService, professorService, courseService));
        invoker.registerCommand(5, createExitCommand());

        return invoker;
    }
}
