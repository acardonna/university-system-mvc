package com.solvd.university.service.interfaces;

import com.solvd.university.model.Building;
import java.util.List;

public interface BuildingService {
    Building addBuilding(String name, Integer universityId);

    Building getBuildingById(Integer buildingId);

    Building getBuildingByName(String name);

    List<Building> getAllBuildings();

    List<Building> getBuildingsByUniversity(Integer universityId);

    void updateBuilding(Building building);

    void deleteBuilding(Integer buildingId);
}
