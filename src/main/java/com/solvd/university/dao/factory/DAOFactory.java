package com.solvd.university.dao.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.solvd.university.dao.impl.*;
import com.solvd.university.dao.interfaces.*;

public final class DAOFactory {

    private static final Map<Class<?>, Supplier<?>> REGISTRY = new HashMap<>();

    static {
                REGISTRY.put(UniversityDAO.class, UniversityDAOImpl::new);
        REGISTRY.put(PersonDAO.class, PersonDAOImpl::new);
        REGISTRY.put(EnrollmentStatusDAO.class, EnrollmentStatusDAOImpl::new);
        REGISTRY.put(GradeLevelDAO.class, GradeLevelDAOImpl::new);
        REGISTRY.put(CourseDifficultyDAO.class, CourseDifficultyDAOImpl::new);
        REGISTRY.put(StudentGradeDAO.class, StudentGradeDAOImpl::new);
        REGISTRY.put(CourseGradeDAO.class, CourseGradeDAOImpl::new);

                REGISTRY.put(BuildingDAO.class, BuildingDAOImpl::new);
        REGISTRY.put(StaffDAO.class, StaffDAOImpl::new);

                REGISTRY.put(DepartmentDAO.class, DepartmentDAOImpl::new);
        REGISTRY.put(ProfessorDAO.class, () -> new ProfessorDAOImpl(create(DepartmentDAO.class)));
        REGISTRY.put(ClassroomDAO.class, () -> new ClassroomDAOImpl(create(BuildingDAO.class)));

                REGISTRY.put(ProgramDAO.class, () -> new ProgramDAOImpl(create(DepartmentDAO.class)));

                REGISTRY.put(EnrollmentDAO.class, () -> new EnrollmentDAOImpl(create(ProgramDAO.class)));

                REGISTRY.put(
            StudentDAO.class,
            () -> new StudentDAOImpl(create(ProgramDAO.class), create(EnrollmentDAO.class))
        );

                REGISTRY.put(
            CourseDAO.class,
            () -> new CourseDAOImpl(
                create(DepartmentDAO.class),
                create(ProfessorDAO.class),
                create(ClassroomDAO.class)
            )
        );

                REGISTRY.put(MyBatisProgramDAO.class, MyBatisProgramDAOImpl::new);
        REGISTRY.put(MyBatisEnrollmentStatusDAO.class, MyBatisEnrollmentStatusDAOImpl::new);
    }

    private DAOFactory() {}

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> daoType) {
        Supplier<?> supplier = REGISTRY.get(daoType);
        if (supplier == null) {
            throw new IllegalArgumentException("No DAO registered for " + daoType.getName());
        }
        return (T) supplier.get();
    }
}
