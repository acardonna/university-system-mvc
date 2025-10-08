package com.solvd.university.model;

import java.util.Objects;

public class CourseDifficulty {

    private Integer courseDifficultyId;
    private String displayName;
    private Integer lvl;

    public static final CourseDifficulty INTRODUCTORY = new CourseDifficulty(3, "Lower-Intermediate", 3);
    public static final CourseDifficulty INTERMEDIATE = new CourseDifficulty(4, "Intermediate", 4);
    public static final CourseDifficulty ADVANCED = new CourseDifficulty(6, "Challenging", 6);
    public static final CourseDifficulty GRADUATE = new CourseDifficulty(8, "Very Hard", 8);

    public CourseDifficulty() {}

    public CourseDifficulty(Integer courseDifficultyId, String displayName, Integer lvl) {
        this.courseDifficultyId = courseDifficultyId;
        this.displayName = displayName;
        this.lvl = lvl;
    }

    public CourseDifficulty(String displayName, Integer lvl) {
        this.displayName = displayName;
        this.lvl = lvl;
    }

    public Integer getCourseDifficultyId() {
        return courseDifficultyId;
    }

    public void setCourseDifficultyId(Integer courseDifficultyId) {
        this.courseDifficultyId = courseDifficultyId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getLevel() {
        return lvl;
    }

    public void setLevel(Integer lvl) {
        this.lvl = lvl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseDifficulty that = (CourseDifficulty) o;

        if (courseDifficultyId == null || that.courseDifficultyId == null) {
            return Objects.equals(lvl, that.lvl);
        }
        return Objects.equals(courseDifficultyId, that.courseDifficultyId);
    }

    @Override
    public int hashCode() {
        return courseDifficultyId != null ? Objects.hash(courseDifficultyId) : Objects.hash(lvl);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
