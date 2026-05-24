package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.util.List;

public class MinDurationFunction implements SleepAnalysisFunction<Long> {
    private static final String DESCRIPTION = "Минимальная продолжительность сессии (минуты)";

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long minDuration = sessions.stream()
                .map(SleepingSession::getDurationMinutes)
                .min(Long::compareTo)
                .orElse(0L);
        return new SleepAnalysisResult<>(DESCRIPTION, minDuration);
    }
}
