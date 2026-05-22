package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.util.List;

public class AvgDurationFunction implements SleepAnalysisFunction<Double> {
    @Override
    public SleepAnalysisResult<Double> apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Средняя продолжительность сессии (минуты)", 0.0);
        }
        double avgDuration = sessions.stream()
            .mapToLong(SleepingSession::getDurationMinutes)
            .average()
            .orElse(0.0);
        return new SleepAnalysisResult<>("Средняя продолжительность сессии (минуты)", avgDuration);
    }
}
