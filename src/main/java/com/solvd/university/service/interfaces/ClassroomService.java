package com.solvd.university.service.interfaces;

import com.solvd.university.model.Classroom;
import java.util.List;

public interface ClassroomService {
    void addClassroom(Classroom classroom);

    Classroom getClassroomByRoomNumber(String roomNumber);

    List<Classroom> getAllClassrooms();

    void updateClassroom(Classroom classroom);

    void deleteClassroom(Integer classroomId);
}
