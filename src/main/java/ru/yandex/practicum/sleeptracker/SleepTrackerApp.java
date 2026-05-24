package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.analysis.functions.*;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SleepTrackerApp {
    private static final List<SleepAnalysisFunction<?>> FUNCTIONS = List.of(
        new SessionCountFunction(),
        new MinDurationFunction(),
        new MaxDurationFunction(),
        new AvgDurationFunction(),
        new BadQualityCountFunction(),
        new SleeplessNightsCountFunction(),
        new ChronotypeFunction()
    );

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Пожалуйста, укажите путь к файлу с логом сна");
            System.exit(1);
        }
        try {
            List<SleepingSession> sessions = loadSessions(args[0]);
            System.out.println("Загружено сессий сна: " + sessions.size());
            System.out.println("=====================================");
            for (SleepAnalysisFunction<?> f : FUNCTIONS) {
                System.out.println(f.apply(sessions));
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            System.exit(1);
        }
    }

    private static List<SleepingSession> loadSessions(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        List<SleepingSession> sessions = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        for (String line : lines) {
            if (line.isBlank()) continue;
            String[] parts = line.split(";");
            if (parts.length != 3) continue;
            sessions.add(new SleepingSession(
                LocalDateTime.parse(parts[0], fmt),
                LocalDateTime.parse(parts[1], fmt),
                SleepingSession.SleepQuality.valueOf(parts[2])
            ));
        }
        return sessions;
    }
}
