package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.analysis.functions.*;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SleepTrackerApp {
    
    private static final List<SleepAnalysisFunction<?>> analysisFunctions = new ArrayList<>();
    
    static {
        analysisFunctions.add(new SessionCountFunction());
        analysisFunctions.add(new MinDurationFunction());
        analysisFunctions.add(new MaxDurationFunction());
        analysisFunctions.add(new AvgDurationFunction());
        analysisFunctions.add(new BadQualityCountFunction());
        analysisFunctions.add(new SleeplessNightsCountFunction());
        analysisFunctions.add(new ChronotypeFunction());
    }
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Пожалуйста, укажите путь к файлу с логом сна");
            System.exit(1);
        }
        
        String filePath = args[0];
        
        try {
            List<SleepingSession> sessions = loadSessionsFromFile(filePath);
            System.out.println("Загружено сессий сна: " + sessions.size());
            System.out.println("=====================================");
            
            for (SleepAnalysisFunction<?> function : analysisFunctions) {
                SleepAnalysisResult<?> result = function.apply(sessions);
                System.out.println(result);
            }
            
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Ошибка при обработке данных: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static List<SleepingSession> loadSessionsFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        List<String> lines = Files.readAllLines(path);
        List<SleepingSession> sessions = new ArrayList<>();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            
            String[] parts = line.split(";");
            if (parts.length != 3) {
                System.err.println("Неверный формат строки: " + line);
                continue;
            }
            
            try {
                LocalDateTime startTime = LocalDateTime.parse(parts[0], formatter);
                LocalDateTime endTime = LocalDateTime.parse(parts[1], formatter);
                SleepingSession.SleepQuality quality = SleepingSession.SleepQuality.valueOf(parts[2]);
                
                sessions.add(new SleepingSession(startTime, endTime, quality));
            } catch (Exception e) {
                System.err.println("Ошибка при парсинге строки: " + line);
            }
        }
        
        return sessions;
    }
}
