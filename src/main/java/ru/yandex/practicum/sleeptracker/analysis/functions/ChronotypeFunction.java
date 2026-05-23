package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.Chronotype;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalysisFunction<String> {

    private static final LocalTime OWL_START_THRESHOLD = LocalTime.of(23, 0);
    private static final LocalTime OWL_END_THRESHOLD = LocalTime.of(9, 0);
    private static final LocalTime LARK_START_THRESHOLD = LocalTime.of(22, 0);
    private static final LocalTime LARK_END_THRESHOLD = LocalTime.of(7, 0);
    private static final LocalTime CHRONOTYPE_VALIDATION_START = LocalTime.of(23, 0);
    private static final LocalTime CHRONOTYPE_VALIDATION_END = LocalTime.of(9, 0);

    @Override
    public SleepAnalysisResult<String> apply(List<SleepingSession> sessions) {
        List<SleepingSession> nightSessions = sessions.stream()
            .filter(SleepingSession::isNightSession)
            .filter(this::isValidForChronotype)
            .collect(Collectors.toList());

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult<>("Хронотип пользователя", Chronotype.DOVE.getRussianName());
        }

        Map<Chronotype, Long> chronotypeCount = nightSessions.stream()
            .map(this::classifyNight)
            .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        Chronotype resultChronotype = chronotypeCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(Chronotype.DOVE);

        long maxCount = chronotypeCount.getOrDefault(resultChronotype, 0L);
        boolean hasTie = chronotypeCount.values().stream()
            .filter(count -> count.equals(maxCount))
            .count() > 1;

        if (hasTie) {
            resultChronotype = Chronotype.DOVE;
        }

        return new SleepAnalysisResult<>("Хронотип пользователя", resultChronotype.getRussianName());
    }

    private boolean isValidForChronotype(SleepingSession session) {
        LocalTime startTime = session.getStartTime().toLocalTime();
        LocalTime endTime = session.getEndTime().toLocalTime();
        return startTime.isBefore(CHRONOTYPE_VALIDATION_START) || endTime.isAfter(CHRONOTYPE_VALIDATION_END);
    }

    private Chronotype classifyNight(SleepingSession session) {
        LocalTime startTime = session.getStartTime().toLocalTime();
        LocalTime endTime = session.getEndTime().toLocalTime();

        boolean isOwl = startTime.isAfter(OWL_START_THRESHOLD) && endTime.isAfter(OWL_END_THRESHOLD);
        boolean isLark = startTime.isBefore(LARK_START_THRESHOLD) && endTime.isBefore(LARK_END_THRESHOLD);

        if (isOwl) return Chronotype.OWL;
        if (isLark) return Chronotype.LARK;
        return Chronotype.DOVE;
    }
}
