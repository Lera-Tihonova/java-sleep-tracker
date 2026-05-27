package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.util.List;

public class MaxDurationFunction implements SleepAnalysisFunction<Long> {
    private static final String DESCRIPTION = "Максимальная продолжительность сессии (минуты)";

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long max = sessions.stream().mapToLong(SleepingSession::getDurationMinutes).max().orElse(0);
        return new SleepAnalysisResult<>(DESCRIPTION, max);
    }
}
