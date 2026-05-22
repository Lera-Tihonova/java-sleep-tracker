package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.util.List;

public class SessionCountFunction implements SleepAnalysisFunction<Integer> {
    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult<>("Общее количество сессий сна", sessions.size());
    }
}
