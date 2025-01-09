package com.example.project.emotionCore.enums;

public enum WorkType {
    POEM,
    NOVEL,
    WEBTOON;

    public static WorkType fromString(String type) {
        try {
            return WorkType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid work type: " + type);
        }
    }
}
