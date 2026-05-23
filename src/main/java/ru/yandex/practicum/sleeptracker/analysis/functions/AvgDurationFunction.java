package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.util.List;

public class AvgDurationFunction implements SleepAnalysisFunction<Double> {

    private static final String DESCRIPTION = "Средняя продолжительность сессии (минуты)";

    @Override
    public SleepAnalysisResult<Double> apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, 0.0);
        }
        double avgDuration = sessions.stream()
            .mapToLong(SleepingSession::getDurationMinutes)
            .average()
            .orElse(0.0);
        return new SleepAnalysisResult<>(DESCRIPTION, avgDuration);
    }
}
