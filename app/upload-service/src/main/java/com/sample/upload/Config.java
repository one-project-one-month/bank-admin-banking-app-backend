package com.sample.upload;

import java.util.Objects;

public class Config {
    public static final String GITHUB_TOKEN = getEnv("GITHUB_TOKEN");
    public static final String GITHUB_OWNER = getEnv("GITHUB_OWNER");
    public static final String GITHUB_REPO = getEnv("GITHUB_REPO");
    public static final String GITHUB_TARGET_FOLDER = getEnv("GITHUB_TARGET_FOLDER");

    private static String getEnv(String name) {
        String value = System.getenv(name);
        Objects.requireNonNull(value, "Environment variable not set: " + name);
        return value;
    }
}
