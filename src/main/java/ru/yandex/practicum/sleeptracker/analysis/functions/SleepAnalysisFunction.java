package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.util.List;

@FunctionalInterface
public interface SleepAnalysisFunction<T> {
    SleepAnalysisResult<T> apply(List<SleepingSession> sessions);
}
