package com.solvd.university.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.dao.factory.DAOFactory;
import com.solvd.university.dao.interfaces.BuildingDAO;
import com.solvd.university.model.Building;
import com.solvd.university.service.interfaces.BuildingService;

public class BuildingServiceImpl implements BuildingService {

    private static final Logger LOGGER = LogManager.getLogger(BuildingServiceImpl.class);
    private final BuildingDAO buildingDAO;

    public BuildingServiceImpl() {
        this.buildingDAO = DAOFactory.create(BuildingDAO.class);
    }

    @Override
    public Building addBuilding(String name, Integer universityId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Building name cannot be null or empty");
        }

        Optional<Building> existing = buildingDAO.findByName(name);
        if (existing.isPresent()) {
            LOGGER.warn("Building with name '{}' already exists", name);
            return existing.get();
        }

        Building building = new Building(name);
        building.setUniversityId(universityId);
        buildingDAO.save(building);
        LOGGER.info("Building '{}' added successfully", name);
        return building;
    }

    @Override
    public Building getBuildingById(Integer buildingId) {
        if (buildingId == null) {
            throw new IllegalArgumentException("Building ID cannot be null");
        }

        return buildingDAO
            .findById(buildingId)
            .orElseThrow(() -> new RuntimeException("Building not found with ID: " + buildingId));
    }

    @Override
    public Building getBuildingByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Building name cannot be null or empty");
        }

        return buildingDAO
            .findByName(name)
            .orElseThrow(() -> new RuntimeException("Building not found with name: " + name));
    }

    @Override
    public List<Building> getAllBuildings() {
        return buildingDAO.findAll();
    }

    @Override
    public List<Building> getBuildingsByUniversity(Integer universityId) {
        if (universityId == null) {
            throw new IllegalArgumentException("University ID cannot be null");
        }

        return buildingDAO.findByUniversity(universityId);
    }

    @Override
    public void updateBuilding(Building building) {
        if (building == null) {
            throw new IllegalArgumentException("Building cannot be null");
        }
        if (building.getBuildingId() == null) {
            throw new IllegalArgumentException("Building ID cannot be null for update");
        }
        if (building.getName() == null || building.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Building name cannot be null or empty");
        }

        buildingDAO.update(building);
        LOGGER.info("Building '{}' updated successfully", building.getName());
    }

    @Override
    public void deleteBuilding(Integer buildingId) {
        if (buildingId == null) {
            throw new IllegalArgumentException("Building ID cannot be null");
        }

        buildingDAO.delete(buildingId);
        LOGGER.info("Building with ID {} deleted successfully", buildingId);
    }
}
