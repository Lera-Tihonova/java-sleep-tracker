package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.Chronotype;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalysisFunction<String> {
    private static final String DESCRIPTION = "Хронотип пользователя";

    @Override
    public SleepAnalysisResult<String> apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, Chronotype.DOVE.getRussianName());
        }

        List<SleepingSession> nightSessions = sessions.stream()
                .filter(s -> {
                    LocalDateTime start = s.getStartTime();
                    LocalDateTime end = s.getEndTime();
                    LocalDate night = getNightDate(start);
                    LocalDateTime nightStart = night.atStartOfDay();
                    LocalDateTime nightEnd = nightStart.plusHours(6);
                    if (start.isBefore(nightEnd) && end.isAfter(nightStart)) {
                        return true;
                    }
                    LocalDate nextNight = night.plusDays(1);
                    LocalDateTime nextNightStart = nextNight.atStartOfDay();
                    LocalDateTime nextNightEnd = nextNightStart.plusHours(6);
                    return start.isBefore(nextNightEnd) && end.isAfter(nextNightStart);
                })
                .collect(Collectors.toList());

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, Chronotype.DOVE.getRussianName());
        }

        Map<Chronotype, Long> counts = nightSessions.stream()
                .map(s -> {
                    LocalTime start = s.getStartTime().toLocalTime();
                    LocalTime end = s.getEndTime().toLocalTime();
                    if (start.isAfter(LocalTime.of(23, 0)) && end.isAfter(LocalTime.of(9, 0))) {
                        return Chronotype.OWL;
                    }
                    if (start.isBefore(LocalTime.of(22, 0)) && end.isBefore(LocalTime.of(7, 0))) {
                        return Chronotype.LARK;
                    }
                    return Chronotype.DOVE;
                })
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        Chronotype result = Chronotype.DOVE;
        long max = 0;
        boolean tie = false;
        for (Map.Entry<Chronotype, Long> e : counts.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                result = e.getKey();
                tie = false;
            } else if (e.getValue().equals(max)) {
                tie = true;
            }
        }
        if (tie) {
            result = Chronotype.DOVE;
        }

        return new SleepAnalysisResult<>(DESCRIPTION, result.getRussianName());
    }

    private LocalDate getNightDate(LocalDateTime dateTime) {
        if (dateTime.getHour() < 12) {
            return dateTime.toLocalDate().minusDays(1);
        }
        return dateTime.toLocalDate();
    }
}
