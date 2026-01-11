package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Platform {

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");

    private final String testFolder;

    private final Map<String, Exam> exams = new HashMap<>();
    private final Map<String, Student> students = new HashMap<>();

    // constructor
    public Platform(String testFolder) {
        this.testFolder = testFolder;
    }

    public void handleCommand(LocalDateTime ts, String cmd, List<String> p) {
        String command = cmd.trim();
        if (command.equals("create_exam")) {
            createExam(p);
        } else if (command.equals("add_question")) {
            addQuestion(ts, p);
        } else if (command.equals("list_questions_history")) {
            writeQuestionHistory(ts, p);
        } else if (command.equals("print_exam")) {
            writeExam(ts, p);
        } else if (command.equals("register_student")) {
            registerStudent(p);
        } else if (command.equals("submit_exam")) {
            submitExam(ts, p);
        } else if (command.equals("show_student_score")) {
            showStudentScore(ts, p);
        } else if (command.equals("generate_report")) {
            generateReport(ts, p);
        } else {
            String nothing = "";
        }
    }

    private void createExam(List<String> p) {
        String examName = p.get(0);
        LocalDateTime start = LocalDateTime.parse(p.get(1), format);
        LocalDateTime end = LocalDateTime.parse(p.get(2), format);
        exams.put(examName, new Exam(examName, start, end));
    }

    private void addQuestion(LocalDateTime ts, List<String> p) {
        String type = p.get(0);
        String examName = p.get(1);
        String askerName = p.get(2);
        int difficulty = Integer.parseInt(p.get(3));
        double maxGrade = Double.parseDouble(p.get(4));
        String questionText = p.get(5);
        String correct = p.get(6);

        Exam exam = exams.get(examName);

        Question<?> q;
        if (type.equals("multiple_choice")) {
            ResponseOption opt = ResponseOption.valueOf(correct.trim());
            q = new MultipleChoiceQuestion(questionText, askerName, difficulty, maxGrade, ts, opt);
        } else if (type.equals("open_ended")) {
            q = new OpenEndedQuestion(questionText, askerName, difficulty, maxGrade, ts, correct);
        } else { // fill_in_the_blank
            q = new FillInTheBlankQuestion(questionText, askerName, difficulty, maxGrade, ts, correct);
        }

        exam.addQuestion(q);
    }

    private void writeQuestionHistory(LocalDateTime ts, List<String> p) {
        String examName = p.get(0);
        Exam exam = exams.get(examName);

        String outFile = testFolder + "/questions_history_" + examName + "_" + ts.format(format) + ".out";

        List<Question<?>> list = exam.getQuestionHistorySorted();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outFile))) {
            for (Question<?> q : list) {

                String correctStr = getCorrectAnswerString(q);

                bw.write(q.getDate().format(format) + " | "
                        + q.getQuestionText() + " | "
                        + correctStr + " | "
                        + q.getDifficulty() + " | "
                        + String.format(Locale.US, "%.1f", q.getMaxGrade()) + " | "
                        + q.getAskerName());
                bw.newLine();
            }
        } catch (IOException ignored) {
        }
    }

    private void writeExam(LocalDateTime ts, List<String> p) {
        String examName = p.get(0);
        Exam exam = exams.get(examName);

        String outFile = testFolder + "/print_exam_" + examName + "_" + ts.format(format) + ".out";

        List<Question<?>> list = exam.getPrintExamQuestionsSorted();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outFile))) {
            for (Question<?> q : list) {

                String correctStr = getCorrectAnswerString(q);

                bw.write(String.format(Locale.US, "%.1f", q.getMaxGrade()) + " | "
                        + q.getQuestionText() + " | "
                        + q.getDifficulty() + " | "
                        + correctStr);
                bw.newLine();
            }
        } catch (IOException ignored) {
        }
    }

    private void registerStudent(List<String> p) {
        String studentName = p.get(0);
        String group = p.get(1);

        students.put(studentName, new Student(studentName, group));
    }

    private void submitExam(LocalDateTime ts, List<String> p) {
        String examName = p.get(0);
        String studentName = p.get(1);

        Exam exam = exams.get(examName);
        Student student = students.get(studentName);

        try {
            gradeSubmission(ts, p, exam, student);
        } catch (SubmissionOutsideTimeIntervalException e) {
            exam.setStudentScore(studentName, 0.0);
            student.setScore(examName, 0.0);
            appendConsole(e.toString());
        }
    }

    private void gradeSubmission(LocalDateTime ts, List<String> p, Exam exam, Student student) throws SubmissionOutsideTimeIntervalException {
        if (ts.isBefore(exam.getStartTime()) || ts.isAfter(exam.getEndTime())) {
            throw new SubmissionOutsideTimeIntervalException(ts, student.getName());
        }

        ArrayList<String> answers = new ArrayList<>();
        for (int i = 2; i < p.size(); i++) {
            String temp = p.get(i);
            answers.add(temp);
        }

        List<Question<?>> orderedQuestions = exam.getPrintExamQuestionsSorted();

        double total = 0.0;
        int n = Math.min(orderedQuestions.size(), answers.size());

        for (int i = 0; i < n; i++) {
            Question<?> q = orderedQuestions.get(i);
            String ans = answers.get(i);

            Correctness correctness;
            if (q instanceof MultipleChoiceQuestion) {
                ResponseOption opt = ResponseOption.valueOf(ans.trim());
                correctness = ((MultipleChoiceQuestion) q).checkAnswer(opt);
            } else if (q instanceof OpenEndedQuestion) {
                correctness = ((OpenEndedQuestion) q).checkAnswer(ans);
            } else if (q instanceof FillInTheBlankQuestion) {
                correctness = ((FillInTheBlankQuestion) q).checkAnswer(ans);
            } else {
                correctness = Correctness.INCORRECT;
            }

            double percent = q.grade(correctness);
            total = total + percent * q.getMaxGrade();
        }

        exam.setStudentScore(student.getName(), total);
        student.setScore(exam.getName(), total);
    }

    private void showStudentScore(LocalDateTime ts, List<String> p) {
        String studentName = p.get(0);
        String examName = p.get(1);

        Student student = students.get(studentName);
        double score = student.getScore(examName);

        String line = ts.format(format)
                + " | The score of " + studentName
                + " for exam " + examName
                + " is " + String.format(Locale.US, "%.2f", score);

        appendConsole(line);
    }

    private void generateReport(LocalDateTime ts, List<String> p) {
        String examName = p.get(0);
        Exam exam = exams.get(examName);

        String outFile = testFolder + "/exam_report_" + examName + "_" + ts.format(format) + ".out";

        List<Map.Entry<String, Double>> list = exam.getStudentScoresAlphabetical();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outFile))) {
            for (Map.Entry<String, Double> e : list) {
                bw.write(e.getKey() + " | " + String.format(Locale.US, "%.2f", e.getValue()));
                bw.newLine();
            }
        } catch (IOException ignored) {
        }
    }

    private void appendConsole(String line) {
        String consoleFile = testFolder + "/console.out";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(consoleFile, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException ignored) { }
    }

    private String getCorrectAnswerString(Question<?> q) {
        if (q instanceof MultipleChoiceQuestion) {
            return ((MultipleChoiceQuestion) q).getCorrectAnswer().name();
        }
        if (q instanceof OpenEndedQuestion) {
            return ((OpenEndedQuestion) q).getCorrectAnswer();
        }
        if (q instanceof FillInTheBlankQuestion) {
            return ((FillInTheBlankQuestion) q).getCorrectAnswer();
        }
        return "";
    }
}
