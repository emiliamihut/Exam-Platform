package org.example;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private static final DateTimeFormatter format =
            DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");

    public static void main(String[] args) {


        String testFolder = "src/main/resources/" + args[0];
        String inputFile = testFolder + "/input.in";
        //creeam paltforma
        Platform platform = new Platform(testFolder);
        // citim inputul
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }
                String timestamp = parts[0];
                String cmd = parts[1];
                if ("exit".equals(cmd)) {
                    break;
                }
                LocalDateTime ts = LocalDateTime.parse(timestamp, format);
                List<String> params = new ArrayList<>();
                for (int i = 2; i < parts.length; i++) {
                    params.add(parts[i]);
                }
                platform.handleCommand(ts, cmd, params);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}