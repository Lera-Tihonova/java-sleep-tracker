package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleeplessNightsCountFunction implements SleepAnalysisFunction<Integer> {
    private static final String DESCRIPTION = "Количество бессонных ночей";

    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, 0);
        }

        LocalDateTime firstStart = sessions.stream()
                .map(SleepingSession::getStartTime)
                .min(LocalDateTime::compareTo)
                .get();

        LocalDateTime lastEnd = sessions.stream()
                .map(SleepingSession::getEndTime)
                .max(LocalDateTime::compareTo)
                .get();

        LocalDate firstNight = getNightDate(firstStart);
        LocalDate lastNight = getNightDate(lastEnd);

        Set<LocalDate> allNights = Stream.iterate(firstNight, d -> d.plusDays(1))
                .limit(lastNight.toEpochDay() - firstNight.toEpochDay() + 1)
                .collect(Collectors.toSet());

        Set<LocalDate> nightsWithSleep = sessions.stream()
                .flatMap(session -> getNightsCovered(session).stream())
                .collect(Collectors.toSet());

        return new SleepAnalysisResult<>(DESCRIPTION, allNights.size() - nightsWithSleep.size());
    }

    private LocalDate getNightDate(LocalDateTime dateTime) {
        if (dateTime.toLocalTime().isBefore(LocalTime.NOON)) {
            return dateTime.toLocalDate().minusDays(1);
        }
        return dateTime.toLocalDate();
    }

    private Set<LocalDate> getNightsCovered(SleepingSession session) {
        Set<LocalDate> nights = new HashSet<>();
        LocalDateTime start = session.getStartTime();
        LocalDateTime end = session.getEndTime();
        LocalDate currentNight = getNightDate(start).minusDays(1);
        LocalDate lastNight = getNightDate(end).plusDays(1);

        while (currentNight.isBefore(lastNight)) {
            LocalDateTime nightStart = currentNight.atStartOfDay();
            LocalDateTime nightEnd = nightStart.plusHours(6);
            if (start.isBefore(nightEnd) && end.isAfter(nightStart)) {
                nights.add(currentNight);
            }
            currentNight = currentNight.plusDays(1);
        }
        return nights;
    }
}
