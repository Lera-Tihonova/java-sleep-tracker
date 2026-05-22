package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.util.List;

public class MaxDurationFunction implements SleepAnalysisFunction<Long> {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Максимальная продолжительность сессии (минуты)", 0L);
        }
        long maxDuration = sessions.stream()
            .map(SleepingSession::getDurationMinutes)
            .max(Long::compareTo)
            .orElse(0L);
        return new SleepAnalysisResult<>("Максимальная продолжительность сессии (минуты)", maxDuration);
    }
}
