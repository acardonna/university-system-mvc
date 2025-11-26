package com.solvd.university;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.solvd.university.model.Building;

public class BuildingTest {

    @Test
    public void verifyBuildingNameTest() {
        Building building = new Building("Science Hall");
        Assert.assertEquals(building.getName(), "Science Hall", "Building name should match constructor argument");
    }

    @Test
    public void verifyBuildingIdSetterTest() {
        Building building = new Building();
        building.setBuildingId(101);
        Assert.assertEquals(building.getBuildingId(), Integer.valueOf(101), "Building ID should be set correctly");
    }

    @Test
    public void verifyUniversityIdSetterTest() {
        Building building = new Building();
        building.setUniversityId(500);
        Assert.assertEquals(building.getUniversityId(), Integer.valueOf(500), "University ID should be set correctly");
    }

    @Test
    public void verifyBuildingToStringTest() {
        Building building = new Building("Library");
        String str = building.toString();
        Assert.assertTrue(str.contains("Library"), "toString should contain the building name");
    }

    @Test
    public void verifyBuildingEqualityTest() {
        Building b1 = new Building("Gym");
        Building b2 = new Building("Gym");
        Assert.assertEquals(b1, b2, "Buildings with the same name should be equal");
    }
}
