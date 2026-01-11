package org.example;

import java.time.LocalDateTime;

public class OpenEndedQuestion extends Question<String> {
    private String correctAnswer;

    public OpenEndedQuestion(String questionText, String askerName, int difficulty, double maxGrade, LocalDateTime date, String correctAnswer) {
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
        if (len < (int) (correctLength - 0.3 * correctLength) || len > (int) (correctLength + 0.3 * correctLength))
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
        if (correctness == Correctness.PARTIAL) return 0.7;
        return 0.0;
    }
}
