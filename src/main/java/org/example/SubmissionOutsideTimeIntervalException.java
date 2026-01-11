package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SubmissionOutsideTimeIntervalException extends Exception {
    private final LocalDateTime timestamp;
    private final String studentName;

    private static final DateTimeFormatter format =
            DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");

    public SubmissionOutsideTimeIntervalException(LocalDateTime timestamp, String studentName) {
        this.timestamp = timestamp;
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return timestamp.format(format) + " | Submission outside of time interval for student " + studentName;
    }
}
