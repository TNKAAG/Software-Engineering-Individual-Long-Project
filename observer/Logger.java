package observer;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Logger implements Observer {

    private static Logger instance;
    private List<String> logs;
    private final String logFile = "smart_home_log.txt";

    private Logger() {
        logs = new ArrayList<>();
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    @Override
    public void update(String message) {
        String timestampedMessage = "[" + LocalDateTime.now() + "] " + message;
        logs.add(timestampedMessage);

        // Print to console
        System.out.println("LOG: " + timestampedMessage);

        // Save to file
        saveToFile(timestampedMessage);
    }

    private void saveToFile(String message) {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            System.out.println("Error writing log file: " + e.getMessage());
        }
    }

    public void showLogs() {
        System.out.println("\n===== ACTION LOGS =====");
        if (logs.isEmpty()) {
            System.out.println("No logs yet.");
        } else {
            for (String log : logs) {
                System.out.println(log);
            }
        }
        System.out.println("=======================\n");
    }

    public List<String> getLogs() {
        return logs;
    }
}