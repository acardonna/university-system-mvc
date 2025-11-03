package com.solvd.university.util;

import java.util.Random;

public final class RandomProvider {

    private static final RandomProvider INSTANCE = new RandomProvider();

    private RandomProvider() {}

    public static RandomProvider getInstance() {
        return INSTANCE;
    }

    public Random getRandom() {
        return new Random();
    }
}
