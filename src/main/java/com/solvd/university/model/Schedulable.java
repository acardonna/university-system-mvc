package com.solvd.university.model;

import java.time.LocalDateTime;

public interface Schedulable {
    void schedule(LocalDateTime start, LocalDateTime end, Classroom room);
}
