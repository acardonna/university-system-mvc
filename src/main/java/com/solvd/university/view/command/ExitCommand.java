package com.solvd.university.view.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExitCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ExitCommand.class);

    @Override
    public void execute() {
        LOGGER.info("Thank you for using the University Management System. Goodbye!");
        System.exit(0);
    }

    @Override
    public String getDescription() {
        return "Exit";
    }
}
