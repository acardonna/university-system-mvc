package com.solvd.university.model;

public class Building extends Facility {

    private Integer buildingId;
    private Integer universityId;

    public Building(String name) {
        super(name);
        this.buildingId = null;
        this.universityId = null;
    }

    public Building() {}

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Integer getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Integer universityId) {
        this.universityId = universityId;
    }

    @Override
    public String toString() {
        return "Building{" + "name='" + name + '\'' + '}';
    }
}
