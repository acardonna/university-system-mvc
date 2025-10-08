package com.solvd.university.dao.interfaces;

import com.solvd.university.model.Classroom;
import java.util.List;
import java.util.Optional;

public interface ClassroomDAO {
    void save(Classroom classroom);

    Optional<Classroom> findById(Integer classroomId);

    Optional<Classroom> findByRoomNumber(String roomNumber);

    List<Classroom> findAll();

    void update(Classroom classroom);

    void delete(Integer classroomId);
}
