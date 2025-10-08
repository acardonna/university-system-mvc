package com.solvd.university;

import com.solvd.university.dao.impl.BuildingDAOImpl;
import com.solvd.university.dao.impl.ClassroomDAOImpl;
import com.solvd.university.dao.impl.CourseDAOImpl;
import com.solvd.university.dao.impl.CourseDifficultyDAOImpl;
import com.solvd.university.dao.impl.CourseGradeDAOImpl;
import com.solvd.university.dao.impl.DepartmentDAOImpl;
import com.solvd.university.dao.impl.EnrollmentDAOImpl;
import com.solvd.university.dao.impl.EnrollmentStatusDAOImpl;
import com.solvd.university.dao.impl.GradeLevelDAOImpl;
import com.solvd.university.dao.impl.PersonDAOImpl;
import com.solvd.university.dao.impl.ProfessorDAOImpl;
import com.solvd.university.dao.impl.ProgramDAOImpl;
import com.solvd.university.dao.impl.StaffDAOImpl;
import com.solvd.university.dao.impl.StudentDAOImpl;
import com.solvd.university.dao.impl.StudentGradeDAOImpl;
import com.solvd.university.dao.impl.UniversityDAOImpl;
import com.solvd.university.dao.interfaces.BuildingDAO;
import com.solvd.university.dao.interfaces.ClassroomDAO;
import com.solvd.university.dao.interfaces.CourseDAO;
import com.solvd.university.dao.interfaces.CourseDifficultyDAO;
import com.solvd.university.dao.interfaces.CourseGradeDAO;
import com.solvd.university.dao.interfaces.DepartmentDAO;
import com.solvd.university.dao.interfaces.EnrollmentDAO;
import com.solvd.university.dao.interfaces.EnrollmentStatusDAO;
import com.solvd.university.dao.interfaces.GradeLevelDAO;
import com.solvd.university.dao.interfaces.PersonDAO;
import com.solvd.university.dao.interfaces.ProfessorDAO;
import com.solvd.university.dao.interfaces.ProgramDAO;
import com.solvd.university.dao.interfaces.StaffDAO;
import com.solvd.university.dao.interfaces.StudentDAO;
import com.solvd.university.dao.interfaces.StudentGradeDAO;
import com.solvd.university.dao.interfaces.UniversityDAO;
import com.solvd.university.service.impl.BuildingServiceImpl;
import com.solvd.university.service.impl.ClassroomServiceImpl;
import com.solvd.university.service.impl.CourseDifficultyServiceImpl;
import com.solvd.university.service.impl.CourseGradeServiceImpl;
import com.solvd.university.service.impl.CourseServiceImpl;
import com.solvd.university.service.impl.DepartmentServiceImpl;
import com.solvd.university.service.impl.EnrollmentServiceImpl;
import com.solvd.university.service.impl.EnrollmentStatusServiceImpl;
import com.solvd.university.service.impl.GradeLevelServiceImpl;
import com.solvd.university.service.impl.PersonServiceImpl;
import com.solvd.university.service.impl.ProfessorServiceImpl;
import com.solvd.university.service.impl.ProgramServiceImpl;
import com.solvd.university.service.impl.StaffServiceImpl;
import com.solvd.university.service.impl.StudentGradeServiceImpl;
import com.solvd.university.service.impl.StudentServiceImpl;
import com.solvd.university.service.impl.UniversityServiceImpl;
import com.solvd.university.service.interfaces.BuildingService;
import com.solvd.university.service.interfaces.ClassroomService;
import com.solvd.university.service.interfaces.CourseDifficultyService;
import com.solvd.university.service.interfaces.CourseGradeService;
import com.solvd.university.service.interfaces.CourseService;
import com.solvd.university.service.interfaces.DepartmentService;
import com.solvd.university.service.interfaces.EnrollmentService;
import com.solvd.university.service.interfaces.EnrollmentStatusService;
import com.solvd.university.service.interfaces.GradeLevelService;
import com.solvd.university.service.interfaces.PersonService;
import com.solvd.university.service.interfaces.ProfessorService;
import com.solvd.university.service.interfaces.ProgramService;
import com.solvd.university.service.interfaces.StaffService;
import com.solvd.university.service.interfaces.StudentGradeService;
import com.solvd.university.service.interfaces.StudentService;
import com.solvd.university.service.interfaces.UniversityService;
import com.solvd.university.util.DatabaseInitializer;
import com.solvd.university.view.UserInterface;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        UniversityDAO universityDAO = new UniversityDAOImpl();
        DepartmentDAO departmentDAO = new DepartmentDAOImpl();
        BuildingDAO buildingDAO = new BuildingDAOImpl();
        ProgramDAO programDAO = new ProgramDAOImpl(departmentDAO);
        EnrollmentDAO enrollmentDAO = new EnrollmentDAOImpl(programDAO);
        StudentDAO studentDAO = new StudentDAOImpl(programDAO, enrollmentDAO);
        ClassroomDAO classroomDAO = new ClassroomDAOImpl(buildingDAO);
        ProfessorDAO professorDAO = new ProfessorDAOImpl(departmentDAO);
        CourseDAO courseDAO = new CourseDAOImpl(departmentDAO, professorDAO, classroomDAO);
        StudentGradeDAO studentGradeDAO = new StudentGradeDAOImpl();
        CourseGradeDAO courseGradeDAO = new CourseGradeDAOImpl();
        CourseDifficultyDAO courseDifficultyDAO = new CourseDifficultyDAOImpl();
        EnrollmentStatusDAO enrollmentStatusDAO = new EnrollmentStatusDAOImpl();
        GradeLevelDAO gradeLevelDAO = new GradeLevelDAOImpl();
        PersonDAO personDAO = new PersonDAOImpl();
        StaffDAO staffDAO = new StaffDAOImpl();

        UniversityService universityService = new UniversityServiceImpl(universityDAO);
        DepartmentService departmentService = new DepartmentServiceImpl(departmentDAO);
        BuildingService buildingService = new BuildingServiceImpl(buildingDAO);

        StudentGradeService studentGradeService = new StudentGradeServiceImpl(studentGradeDAO);
        CourseGradeService courseGradeService = new CourseGradeServiceImpl(courseGradeDAO);

        StudentService studentService = new StudentServiceImpl(studentDAO, studentGradeService);

        CourseService courseService = new CourseServiceImpl(courseDAO);

        ProfessorService professorService = new ProfessorServiceImpl(professorDAO, courseService);

        ProgramService programService = new ProgramServiceImpl(programDAO);

        EnrollmentService enrollmentService = new EnrollmentServiceImpl(
            enrollmentDAO,
            studentDAO,
            courseService,
            studentGradeService,
            courseGradeService
        );

        ClassroomService classroomService = new ClassroomServiceImpl(classroomDAO);
        CourseDifficultyService courseDifficultyService = new CourseDifficultyServiceImpl(courseDifficultyDAO);
        EnrollmentStatusService enrollmentStatusService = new EnrollmentStatusServiceImpl(enrollmentStatusDAO);
        GradeLevelService gradeLevelService = new GradeLevelServiceImpl(gradeLevelDAO);
        PersonService personService = new PersonServiceImpl(personDAO);
        StaffService staffService = new StaffServiceImpl(staffDAO);

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
