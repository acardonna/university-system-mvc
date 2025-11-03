package com.solvd.university.service.impl;

import java.util.List;

import com.solvd.university.dao.factory.DAOFactory;
import com.solvd.university.dao.interfaces.ClassroomDAO;
import com.solvd.university.model.Classroom;
import com.solvd.university.service.interfaces.ClassroomService;

public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomDAO classroomDAO;

    public ClassroomServiceImpl() {
        this.classroomDAO = DAOFactory.create(ClassroomDAO.class);
    }

    @Override
    public void addClassroom(Classroom classroom) {
        classroomDAO.save(classroom);
    }

    @Override
    public Classroom getClassroomByRoomNumber(String roomNumber) {
        return classroomDAO.findByRoomNumber(roomNumber).orElse(null);
    }

    @Override
    public List<Classroom> getAllClassrooms() {
        return classroomDAO.findAll();
    }

    @Override
    public void updateClassroom(Classroom classroom) {
        classroomDAO.update(classroom);
    }

    @Override
    public void deleteClassroom(Integer classroomId) {
        classroomDAO.delete(classroomId);
    }
}
