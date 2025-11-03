package com.solvd.university.view.command;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.model.Program;
import com.solvd.university.service.interfaces.ProgramService;

public class BrowseProgramsCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(BrowseProgramsCommand.class);

    private final ProgramService programService;

    public BrowseProgramsCommand(ProgramService programService) {
        this.programService = programService;
    }

    @Override
    public void execute() {
        showAvailableProgramsAsGuest();
    }

    @Override
    public String getDescription() {
        return "Browse available programs";
    }

    private void showAvailableProgramsAsGuest() {
        Map<Object, java.util.List<Program>> programsByDepartment = programService
            .getAllPrograms()
            .stream()
            .filter(program -> program.getDepartment() != null)
            .collect(Collectors.groupingBy(Program::getDepartment));

        LOGGER.info("=== Available Programs ===");
        LOGGER.info("");

        AtomicInteger programOrder = new AtomicInteger(1);

        programsByDepartment
            .entrySet()
            .forEach(entry -> {
                Object dept = entry.getKey();
                LOGGER.info("Department: " + dept.getClass().getSimpleName());

                entry
                    .getValue()
                    .forEach(program -> {
                        LOGGER.info(
                            programOrder.get() +
                                ". " +
                                program.getName() +
                                " - $" +
                                program.getPrice()
                        );
                        programOrder.incrementAndGet();
                    });
                LOGGER.info("");
            });
    }
}
