package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.util.List;

public class MaxDurationFunction implements SleepAnalysisFunction<Long> {
    private static final String DESCRIPTION = "Максимальная продолжительность сессии (минуты)";

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long maxDuration = sessions.stream()
                .map(SleepingSession::getDurationMinutes)
                .max(Long::compareTo)
                .orElse(0L);
        return new SleepAnalysisResult<>(DESCRIPTION, maxDuration);
    }
}
