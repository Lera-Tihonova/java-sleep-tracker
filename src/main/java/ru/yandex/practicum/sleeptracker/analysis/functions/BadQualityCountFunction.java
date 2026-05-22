package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.util.List;

public class BadQualityCountFunction implements SleepAnalysisFunction<Integer> {
    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        long badCount = sessions.stream()
            .filter(session -> session.getQuality() == SleepingSession.SleepQuality.BAD)
            .count();
        return new SleepAnalysisResult<>("Количество сессий с плохим качеством сна", (int) badCount);
    }
}
