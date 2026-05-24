package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.util.List;

public class MinDurationFunction implements SleepAnalysisFunction<Long> {
    private static final String DESCRIPTION = "Минимальная продолжительность сессии (минуты)";

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long min = sessions.stream().mapToLong(SleepingSession::getDurationMinutes).min().orElse(0);
        return new SleepAnalysisResult<>(DESCRIPTION, min);
    }
}
