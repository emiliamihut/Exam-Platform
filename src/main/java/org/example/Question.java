package org.example;

import java.time.LocalDateTime;

abstract class Question<T> implements Gradable {
    private final String questionText;
    private final String askerName;
    private final LocalDateTime date;
    private final int difficulty;
    private final double maxGrade;

    public Question(String questionText, String askerName, int difficulty, double maxGrade, LocalDateTime date) {
        this.questionText = questionText;
        this.askerName = askerName;
        this.difficulty = difficulty;
        this.maxGrade = maxGrade;
        this.date =  date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public double getMaxGrade() {
        return maxGrade;
    }

    public String getAskerName() {
        return askerName;
    }

    public String getQuestionText() {
        return questionText;
    }

    public abstract Correctness checkAnswer(T input);
}
