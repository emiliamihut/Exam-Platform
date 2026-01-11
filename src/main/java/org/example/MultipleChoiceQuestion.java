package org.example;

import java.time.LocalDateTime;

public class MultipleChoiceQuestion extends Question<ResponseOption> {
    private ResponseOption correctAnswer;

    // constructor
    public MultipleChoiceQuestion(String questionText, String askerName, int difficulty, double maxGrade, LocalDateTime date, ResponseOption correctAnswer) {
        super(questionText, askerName, difficulty, maxGrade, date);
        this.correctAnswer = correctAnswer;
    }

    public ResponseOption getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(ResponseOption correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public Correctness checkAnswer(ResponseOption answer) {
        if (answer == null) {
            return Correctness.INCORRECT;
        }
        if(!(answer.equals(correctAnswer)))
            return Correctness.INCORRECT;
        return Correctness.CORRECT;
    }

    @Override
    public double grade(Correctness correctness) {
        if (correctness == Correctness.CORRECT) return 1.0;
        return 0.0;
    }


}
