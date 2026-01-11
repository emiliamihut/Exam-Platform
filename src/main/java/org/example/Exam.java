package org.example;

import java.time.LocalDateTime;
import java.util.*;

public class Exam {
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private final List<Question<?>> questions;
    private final Map<String, Double> scores;

    private static final Comparator<Question<?>> HISTORY_ORDER =
            Comparator.<Question<?>, LocalDateTime>comparing(Question::getDate)
                    .thenComparing(Question::getAskerName);
    private static final Comparator<Question<?>> PRINT_ORDER =
            Comparator.<Question<?>, Integer>comparing(Question::getDifficulty)
                    .thenComparing(Question::getQuestionText);

    // constructor
    public Exam(String name, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.questions = new ArrayList<>();
        this.scores = new LinkedHashMap<>();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    // functionalitati
    public void addQuestion(Question<?> question) {
        questions.add(question);
    }
    public List<Question<?>> getQuestionHistorySorted() {
        List<Question<?>> sorted = new ArrayList<>(questions);
        sorted.sort(HISTORY_ORDER);
        return sorted;
    }
    public List<Question<?>> getPrintExamQuestionsSorted() {
        List<Question<?>> sorted = new ArrayList<>(questions);
        sorted.sort(PRINT_ORDER);
        return sorted;
    }
    public void setStudentScore(String studentName, double score) {
        scores.put(studentName, score);
    }
    public double getStudentScore(String studentName) {
        return scores.getOrDefault(studentName, 0.0);
    }
    public List<Map.Entry<String, Double>> getStudentScoresAlphabetical() {
        List<Map.Entry<String, Double>> list = new ArrayList<>(scores.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getKey));
        return list;
    }
    public Map<String, Double> getScoresView() {
        return Collections.unmodifiableMap(scores);
    }
}
