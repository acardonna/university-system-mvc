package com.solvd.university.service.interfaces;

import java.util.List;

import com.solvd.university.model.Program;

public interface ProgramJsonService {
    List<Program> read();
    void write(List<Program> programs);
    void displayPrograms();
    void demonstrateReadWrite();
}
