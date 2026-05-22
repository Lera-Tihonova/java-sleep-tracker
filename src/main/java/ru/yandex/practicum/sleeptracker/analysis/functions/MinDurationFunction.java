package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.util.List;

public class MinDurationFunction implements SleepAnalysisFunction<Long> {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Минимальная продолжительность сессии (минуты)", 0L);
        }
        long minDuration = sessions.stream()
            .map(SleepingSession::getDurationMinutes)
            .min(Long::compareTo)
            .orElse(0L);
        return new SleepAnalysisResult<>("Минимальная продолжительность сессии (минуты)", minDuration);
    }
}
