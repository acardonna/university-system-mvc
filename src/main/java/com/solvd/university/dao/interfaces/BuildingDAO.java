package com.solvd.university.dao.interfaces;

import com.solvd.university.model.Building;
import java.util.List;
import java.util.Optional;

public interface BuildingDAO {
    void save(Building building);

    Optional<Building> findById(Integer buildingId);

    Optional<Building> findByName(String name);

    List<Building> findAll();

    List<Building> findByUniversity(Integer universityId);

    void update(Building building);

    void delete(Integer buildingId);
}
