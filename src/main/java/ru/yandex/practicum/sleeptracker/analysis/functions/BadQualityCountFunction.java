package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.util.List;

public class BadQualityCountFunction implements SleepAnalysisFunction<Integer> {
    private static final String DESCRIPTION = "Количество сессий с плохим качеством сна";

    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        long count = sessions.stream().filter(s -> s.getQuality() == SleepingSession.SleepQuality.BAD).count();
        return new SleepAnalysisResult<>(DESCRIPTION, (int) count);
    }
}
