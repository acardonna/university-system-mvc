package com.solvd.university.model;

import java.util.Objects;

public class GradeLevel {

    private Integer gradeLevelId;
    private String displayName;
    private Integer year;

    public static final GradeLevel FRESHMAN = new GradeLevel(1, "Freshman", 1);
    public static final GradeLevel SOPHOMORE = new GradeLevel(2, "Sophomore", 2);
    public static final GradeLevel JUNIOR = new GradeLevel(3, "Junior", 3);
    public static final GradeLevel SENIOR = new GradeLevel(4, "Senior", 4);
    public static final GradeLevel GRADUATE = new GradeLevel(5, "Postgrad 1", 5);

    public GradeLevel() {}

    public GradeLevel(Integer gradeLevelId, String displayName, Integer year) {
        this.gradeLevelId = gradeLevelId;
        this.displayName = displayName;
        this.year = year;
    }

    public GradeLevel(String displayName, Integer year) {
        this.displayName = displayName;
        this.year = year;
    }

    public Integer getGradeLevelId() {
        return gradeLevelId;
    }

    public void setGradeLevelId(Integer gradeLevelId) {
        this.gradeLevelId = gradeLevelId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeLevel that = (GradeLevel) o;

        if (gradeLevelId == null || that.gradeLevelId == null) {
            return Objects.equals(year, that.year);
        }
        return Objects.equals(gradeLevelId, that.gradeLevelId);
    }

    @Override
    public int hashCode() {
        return gradeLevelId != null ? Objects.hash(gradeLevelId) : Objects.hash(year);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
