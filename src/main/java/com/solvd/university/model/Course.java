package com.solvd.university.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

import com.solvd.university.model.annotation.RequiredExperience;

@RequiredExperience(level = 2)
public class Course<T, D extends Department<T>> implements Identifiable, Schedulable {

    private Integer courseId;
    private Integer professorId;
    private Integer departmentId;
    private Integer classroomId;
    private Integer courseDifficultyId;
    private Integer universityId;
    private String courseCode;
    private String courseName;
    private int creditHours;
    private Professor professor;
    private D department;
    private final String id;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Classroom classroom;
    private List<Grade<Double>> courseGrades;
    private TreeMap<LocalDateTime, Grade<Double>> gradesByDate;
    private CourseDifficulty difficulty;

    public Course() {
        this.courseId = null;
        this.professorId = null;
        this.departmentId = null;
        this.classroomId = null;
        this.courseDifficultyId = null;
        this.universityId = null;
        this.id = "COURSE-UNASSIGNED";
        this.courseGrades = new ArrayList<>();
        this.gradesByDate = new TreeMap<>();
        this.difficulty = CourseDifficulty.INTRODUCTORY;
    }

    public Course(String courseCode, String courseName, int creditHours, Professor professor, D department) {
        this.courseId = null;
        this.professorId = professor != null ? professor.getProfessorId() : null;
        this.departmentId = department != null ? department.getDepartmentId() : null;
        this.classroomId = null;
        this.courseDifficultyId = CourseDifficulty.INTRODUCTORY.getCourseDifficultyId();
        this.universityId = null;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.professor = professor;
        this.department = department;
        this.id = courseCode;
        this.courseGrades = new ArrayList<>();
        this.gradesByDate = new TreeMap<>();
        this.difficulty = CourseDifficulty.INTRODUCTORY;
    }

    public Course(int courseNumber, String courseName, int creditHours, Professor professor, D department) {
        this.courseId = null;
        this.professorId = professor != null ? professor.getProfessorId() : null;
        this.departmentId = department != null ? department.getDepartmentId() : null;
        this.classroomId = null;
        this.courseDifficultyId = CourseDifficulty.INTRODUCTORY.getCourseDifficultyId();
        this.courseCode = department.getDepartmentCode() + String.valueOf(courseNumber);
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.professor = professor;
        this.department = department;
        this.id = this.courseCode;
        this.courseGrades = new ArrayList<>();
        this.gradesByDate = new TreeMap<>();
        this.difficulty = CourseDifficulty.INTRODUCTORY;
    }

    public Course(
        String courseCode,
        String courseName,
        int creditHours,
        Professor professor,
        D department,
        CourseDifficulty difficulty
    ) {
        this.courseId = null;
        this.professorId = professor != null ? professor.getProfessorId() : null;
        this.departmentId = department != null ? department.getDepartmentId() : null;
        this.classroomId = null;
        this.courseDifficultyId = difficulty != null ? difficulty.getCourseDifficultyId() : null;
        this.universityId = department != null ? department.getUniversityId() : null;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.professor = professor;
        this.department = department;
        this.id = courseCode;
        this.courseGrades = new ArrayList<>();
        this.gradesByDate = new TreeMap<>();
        this.difficulty = difficulty;
    }

    public Course(
        int courseNumber,
        String courseName,
        int creditHours,
        Professor professor,
        D department,
        CourseDifficulty difficulty
    ) {
        this.courseId = null;
        this.professorId = professor != null ? professor.getProfessorId() : null;
        this.departmentId = department != null ? department.getDepartmentId() : null;
        this.classroomId = null;
        this.courseDifficultyId = difficulty != null ? difficulty.getCourseDifficultyId() : null;
        this.universityId = department != null ? department.getUniversityId() : null;
        this.courseCode = department.getDepartmentCode() + String.valueOf(courseNumber);
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.professor = professor;
        this.department = department;
        this.id = this.courseCode;
        this.courseGrades = new ArrayList<>();
        this.gradesByDate = new TreeMap<>();
        this.difficulty = difficulty;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Integer classroomId) {
        this.classroomId = classroomId;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
        this.classroomId = classroom != null ? classroom.getClassroomId() : null;
    }

    public Integer getCourseDifficultyId() {
        return courseDifficultyId;
    }

    public void setCourseDifficultyId(Integer courseDifficultyId) {
        this.courseDifficultyId = courseDifficultyId;
    }

    public Integer getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Integer universityId) {
        this.universityId = universityId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
        this.professorId = professor != null ? professor.getProfessorId() : null;
    }

    public D getDepartment() {
        return department;
    }

    public void setDepartment(D department) {
        this.department = department;
        this.departmentId = department != null ? department.getDepartmentId() : null;
    }

    public CourseDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(CourseDifficulty difficulty) {
        this.difficulty = difficulty;
        this.courseDifficultyId = difficulty != null ? difficulty.getCourseDifficultyId() : null;
    }

    public String getFormattedCourseCode() {
        String deptCode = department != null ? String.valueOf(department.getDepartmentCode()) : "UNKN";
        return String.format("%s (%s)", courseCode, deptCode);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void schedule(LocalDateTime start, LocalDateTime end, Classroom room) {
        this.startAt = start;
        this.endAt = end;
        this.classroom = room;
        this.classroomId = room != null ? room.getClassroomId() : null;
    }

    public LocalDateTime getScheduledStart() {
        return startAt;
    }

    public void setScheduledStart(LocalDateTime start) {
        this.startAt = start;
    }

    public LocalDateTime getScheduledEnd() {
        return endAt;
    }

    public void setScheduledEnd(LocalDateTime end) {
        this.endAt = end;
    }

    public void addGrade(Grade<Double> grade) {
        courseGrades.add(grade);
        gradesByDate.put(LocalDateTime.now(), grade);
    }

    @RequiredExperience(level = 3)
    public void addGrade(Grade<Double> grade, GradeValidator validator) {
        if (validator.isValid(grade.value())) {
            courseGrades.add(grade);
            gradesByDate.put(LocalDateTime.now(), grade);
        } else {
            throw new IllegalArgumentException("Invalid grade value: " + grade.value());
        }
    }

    public void addGradeWithDate(Grade<Double> grade, LocalDateTime date) {
        courseGrades.add(grade);
        gradesByDate.put(date, grade);
    }

    public void addGradeWithDate(Grade<Double> grade, LocalDateTime date, GradeValidator validator) {
        if (validator.isValid(grade.value())) {
            courseGrades.add(grade);
            gradesByDate.put(date, grade);
        } else {
            throw new IllegalArgumentException("Invalid grade value: " + grade.value());
        }
    }

    public List<Grade<Double>> getCourseGrades() {
        return new ArrayList<>(courseGrades);
    }

    public double calculateCourseAverage() {
        if (courseGrades.isEmpty()) {
            return 0.0;
        }
        return courseGrades.stream().mapToDouble(Grade::value).average().orElse(0.0);
    }

    public List<Grade<Double>> getGradesForSemester(String semester) {
        return courseGrades
            .stream()
            .filter(grade -> grade.semester().equals(semester))
            .toList();
    }

    public NavigableMap<LocalDateTime, Grade<Double>> getGradesByDate() {
        return new TreeMap<>(gradesByDate);
    }

    public Grade<Double> getLatestGrade() {
        return gradesByDate.isEmpty() ? null : gradesByDate.lastEntry().getValue();
    }

    public Grade<Double> getEarliestGrade() {
        return gradesByDate.isEmpty() ? null : gradesByDate.firstEntry().getValue();
    }

    public Map<LocalDateTime, Grade<Double>> getGradesInDateRange(LocalDateTime start, LocalDateTime end) {
        return gradesByDate.subMap(start, true, end, true);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Course<?, ?> course = (Course<?, ?>) o;
        return (
            creditHours == course.creditHours &&
            Objects.equals(courseCode, course.courseCode) &&
            Objects.equals(courseName, course.courseName) &&
            Objects.equals(professor, course.professor) &&
            Objects.equals(department, course.department)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode, courseName, creditHours, professor, department);
    }

    @Override
    public String toString() {
        String sched = (startAt != null && endAt != null && classroom != null)
            ? String.format(
                " | %s to %s in %s %s",
                startAt,
                endAt,
                classroom.getBuilding().getName(),
                classroom.getRoomNumber()
            )
            : "";
        String gradeInfo = courseGrades.isEmpty() ? "" : String.format(" | Avg Grade: %.1f", calculateCourseAverage());
        String difficultyInfo = (difficulty != null)
            ? String.format(" | Difficulty: %s", difficulty.getDisplayName())
            : "";
        String deptName = department != null ? department.getName() : "Unassigned";
        String profName = professor != null ? professor.getFullName() : "TBA";
        return String.format(
            "%s - %s | %d Credit Hours | Professor: %s | Department: %s%s%s%s",
            getFormattedCourseCode(),
            courseName,
            creditHours,
            profName,
            deptName,
            difficultyInfo,
            sched,
            gradeInfo
        );
    }

    public static <T, D extends Department<T>> Builder<T, D> builder() {
        return new Builder<>();
    }

    public static final class Builder<T, D extends Department<T>> {

        private Integer courseNumber;
        private String courseCode;
        private String courseName;
        private int creditHours;
        private Professor professor;
        private D department;
        private CourseDifficulty difficulty = CourseDifficulty.INTRODUCTORY;

        public Builder<T, D> number(int number) {
            this.courseNumber = number;
            this.courseCode = null;
            return this;
        }

        public Builder<T, D> code(String code) {
            this.courseCode = code;
            this.courseNumber = null;
            return this;
        }

        public Builder<T, D> name(String name) {
            this.courseName = name;
            return this;
        }

        public Builder<T, D> creditHours(int hours) {
            this.creditHours = hours;
            return this;
        }

        public Builder<T, D> professor(Professor assignedProfessor) {
            this.professor = assignedProfessor;
            return this;
        }

        public Builder<T, D> department(D dept) {
            this.department = dept;
            return this;
        }

        public Builder<T, D> difficulty(CourseDifficulty level) {
            this.difficulty = level != null ? level : CourseDifficulty.INTRODUCTORY;
            return this;
        }

        public Course<T, D> build() {
            if (courseCode != null) {
                return new Course<>(courseCode, courseName, creditHours, professor, department, difficulty);
            }
            if (courseNumber != null) {
                return new Course<>(courseNumber, courseName, creditHours, professor, department, difficulty);
            }
            throw new IllegalStateException("Course code or number must be provided");
        }
    }
}
