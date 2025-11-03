package com.solvd.university.service.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.solvd.university.service.impl.*;
import com.solvd.university.service.interfaces.*;

public final class ServiceFactory {

    private static final Map<Class<?>, Supplier<?>> REGISTRY = new HashMap<>();

    static {
                REGISTRY.put(UniversityService.class, UniversityServiceImpl::new);
        REGISTRY.put(DepartmentService.class, DepartmentServiceImpl::new);
        REGISTRY.put(BuildingService.class, BuildingServiceImpl::new);
        REGISTRY.put(ClassroomService.class, ClassroomServiceImpl::new);
        REGISTRY.put(PersonService.class, PersonServiceImpl::new);
        REGISTRY.put(ProfessorService.class, ProfessorServiceImpl::new);
        REGISTRY.put(StaffService.class, StaffServiceImpl::new);

                REGISTRY.put(StudentService.class, StudentServiceImpl::new);
        REGISTRY.put(EnrollmentService.class, EnrollmentServiceImpl::new);
        REGISTRY.put(EnrollmentStatusService.class, EnrollmentStatusServiceImpl::new);

                REGISTRY.put(ProgramService.class, ProgramServiceImpl::new);
        REGISTRY.put(CourseService.class, CourseServiceImpl::new);
        REGISTRY.put(CourseDifficultyService.class, CourseDifficultyServiceImpl::new);

                REGISTRY.put(StudentGradeService.class, StudentGradeServiceImpl::new);
        REGISTRY.put(CourseGradeService.class, CourseGradeServiceImpl::new);
        REGISTRY.put(GradeLevelService.class, GradeLevelServiceImpl::new);

                REGISTRY.put(XmlParserService.class, XmlParserServiceImpl::new);
        REGISTRY.put(ProgramJsonService.class, ProgramJsonServiceImpl::new);
        REGISTRY.put(EnrollmentStatusJsonService.class, EnrollmentStatusJsonServiceImpl::new);

                REGISTRY.put(MyBatisProgramService.class, MyBatisProgramServiceImpl::new);
        REGISTRY.put(MyBatisEnrollmentStatusService.class, MyBatisEnrollmentStatusServiceImpl::new);
    }

    private ServiceFactory() {}

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> serviceType) {
        Supplier<?> supplier = REGISTRY.get(serviceType);
        if (supplier == null) {
            throw new IllegalArgumentException("No service registered for " + serviceType.getName());
        }
        return (T) supplier.get();
    }
}
