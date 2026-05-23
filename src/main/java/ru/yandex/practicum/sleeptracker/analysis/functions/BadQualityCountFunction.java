package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.util.List;

public class BadQualityCountFunction implements SleepAnalysisFunction<Integer> {
    private static final String DESCRIPTION = "Количество сессий с плохим качеством сна";

    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, 0);
        }
        long badCount = sessions.stream()
            .filter(session -> session.getQuality() == SleepingSession.SleepQuality.BAD)
            .count();
        return new SleepAnalysisResult<>(DESCRIPTION, (int) badCount);
    }
}
