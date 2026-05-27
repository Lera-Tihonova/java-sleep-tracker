package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.util.List;

public class SessionCountFunction implements SleepAnalysisFunction<Integer> {
    private static final String DESCRIPTION = "Общее количество сессий сна";

    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult<>(DESCRIPTION, sessions.size());
    }
}
