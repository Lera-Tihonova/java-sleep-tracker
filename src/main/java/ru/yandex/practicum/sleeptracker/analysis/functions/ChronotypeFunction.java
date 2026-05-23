package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.Chronotype;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalysisFunction<String> {

    private static final LocalTime OWL_START = LocalTime.of(23, 0);
    private static final LocalTime OWL_END = LocalTime.of(9, 0);
    private static final LocalTime LARK_START = LocalTime.of(22, 0);
    private static final LocalTime LARK_END = LocalTime.of(7, 0);

    @Override
    public SleepAnalysisResult<String> apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Хронотип пользователя", Chronotype.DOVE.getRussianName());
        }

        List<SleepingSession> nightSessions = sessions.stream()
            .filter(session -> {
                LocalTime start = session.getStartTime().toLocalTime();
                LocalTime end = session.getEndTime().toLocalTime();
                return start.isAfter(LocalTime.of(20, 0)) || end.isBefore(LocalTime.of(12, 0));
            })
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

    private Chronotype classifyNight(SleepingSession session) {
        LocalTime startTime = session.getStartTime().toLocalTime();
        LocalTime endTime = session.getEndTime().toLocalTime();

        boolean isOwl = startTime.isAfter(OWL_START) && endTime.isAfter(OWL_END);
        boolean isLark = startTime.isBefore(LARK_START) && endTime.isBefore(LARK_END);

        if (isOwl) return Chronotype.OWL;
        if (isLark) return Chronotype.LARK;
        return Chronotype.DOVE;
    }
}
