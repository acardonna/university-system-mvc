package com.solvd.university.service.interfaces;

import com.solvd.university.model.Professor;
import java.util.List;

public interface ProfessorService {
    void addProfessor(Professor professor);

    Professor getProfessorByName(String firstName, String lastName);

    List<Professor> getAllProfessors();

    void updateProfessor(Professor professor);

    void deleteProfessor(Integer professorId);
}
