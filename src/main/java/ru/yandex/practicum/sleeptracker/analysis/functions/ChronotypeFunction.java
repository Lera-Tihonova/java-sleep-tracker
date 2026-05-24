package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.Chronotype;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalysisFunction<String> {
    private static final String DESCRIPTION = "Хронотип пользователя";
    private static final LocalTime OWL_START = LocalTime.of(23, 0);
    private static final LocalTime OWL_END = LocalTime.of(9, 0);
    private static final LocalTime LARK_START = LocalTime.of(22, 0);
    private static final LocalTime LARK_END = LocalTime.of(7, 0);

    @Override
    public SleepAnalysisResult<String> apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, Chronotype.DOVE.getRussianName());
        }

        List<SleepingSession> nightSessions = sessions.stream()
                .filter(SleepingSession::isNightSession)
                .collect(Collectors.toList());

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, Chronotype.DOVE.getRussianName());
        }

        Map<Chronotype, Long> counts = nightSessions.stream()
                .map(s -> {
                    LocalTime start = s.getStartTime().toLocalTime();
                    LocalTime end = s.getEndTime().toLocalTime();
                    if (start.isAfter(OWL_START) && end.isAfter(OWL_END)) {
                        return Chronotype.OWL;
                    }
                    if (start.isBefore(LARK_START) && end.isBefore(LARK_END)) {
                        return Chronotype.LARK;
                    }
                    return Chronotype.DOVE;
                })
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        Chronotype result = Chronotype.DOVE;
        long maxCount = 0;
        for (Map.Entry<Chronotype, Long> entry : counts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                result = entry.getKey();
            } else if (entry.getValue() == maxCount) {
                result = Chronotype.DOVE;
            }
        }

        return new SleepAnalysisResult<>(DESCRIPTION, result.getRussianName());
    }
}
