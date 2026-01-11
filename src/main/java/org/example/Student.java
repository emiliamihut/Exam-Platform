package org.example;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private final String name;
    private String group;

    private final Map<String, Double> scoresAndExams = new HashMap<>();

    public Student(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public Map<String, Double> getScoresAndExams() {
        return scoresAndExams;
    }

    public double getScore(String examName) {
        return scoresAndExams.getOrDefault(examName, 0.0);
    }

    public void setScore(String examName, double score) {
        scoresAndExams.put(examName, score);
    }
}
