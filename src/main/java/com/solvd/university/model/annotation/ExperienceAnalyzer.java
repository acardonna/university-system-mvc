package com.solvd.university.model.annotation;

public class ExperienceAnalyzer {

    public static boolean hasRequirement(Class<?> clazz) {
        return clazz.isAnnotationPresent(RequiredExperience.class);
    }

    public static int getRequiredLevel(Class<?> clazz) {
        if (hasRequirement(clazz)) {
            RequiredExperience annotation = clazz.getAnnotation(RequiredExperience.class);
            return annotation.level();
        }
        return 1;
    }

    public static boolean meetsRequirement(Object student, Class<?> targetClass) {
        int requiredLevel = getRequiredLevel(targetClass);
        int studentLevel = getStudentLevel(student);
        return studentLevel >= requiredLevel;
    }

    private static int getStudentLevel(Object student) {
        try {
            var getGradeLevel = student.getClass().getMethod("getGradeLevel");
            var gradeLevel = getGradeLevel.invoke(student);
            var getYear = gradeLevel.getClass().getMethod("getYear");
            return (Integer) getYear.invoke(gradeLevel);
        } catch (Exception e) {
            return 1;
        }
    }
}
