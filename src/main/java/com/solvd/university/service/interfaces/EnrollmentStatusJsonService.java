package com.solvd.university.service.interfaces;

import java.util.List;

import com.solvd.university.model.EnrollmentStatus;

public interface EnrollmentStatusJsonService {
    List<EnrollmentStatus> read();
    void write(List<EnrollmentStatus> statuses);
    void displayStatuses();
    void demonstrateReadWrite();
}
