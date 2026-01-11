package org.example;

import java.time.LocalDateTime;

public class FillInTheBlankQuestion extends Question<String> {

    private String correctAnswer;

    public FillInTheBlankQuestion(String questionText, String askerName, int difficulty, double maxGrade, LocalDateTime date, String correctAnswer) {
        super(questionText, askerName, difficulty, maxGrade, date);
        this.correctAnswer = correctAnswer;
    }

    @Override
    public Correctness checkAnswer(String answer) {
        if (answer == null)
            return Correctness.INCORRECT;
        if (answer.equals(correctAnswer))
            return Correctness.CORRECT;
        int len = answer.length();
        int correctLength = correctAnswer.length();
        if (len < correctLength - 2 || len > correctLength + 2)
            return Correctness.INCORRECT;
        if (correctAnswer.contains(answer) || answer.contains(correctAnswer))
            return Correctness.PARTIAL;
        return Correctness.INCORRECT;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public double grade(Correctness correctness) {
        if (correctness == Correctness.CORRECT) return 1.0;
        if (correctness == Correctness.PARTIAL) return 0.5;
        return 0.0;
    }
}
