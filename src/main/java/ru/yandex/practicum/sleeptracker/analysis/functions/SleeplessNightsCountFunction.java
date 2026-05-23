package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleeplessNightsCountFunction implements SleepAnalysisFunction<Integer> {
    private static final String DESCRIPTION = "Количество бессонных ночей";
    private static final LocalTime NIGHT_START = LocalTime.of(0, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

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

        LocalDate firstNight = toNightDate(firstStart);
        LocalDate lastNight = toNightDate(lastEnd);

        Set<LocalDate> allNights = Stream.iterate(firstNight, d -> d.plusDays(1))
                .limit(lastNight.toEpochDay() - firstNight.toEpochDay() + 1)
                .collect(Collectors.toSet());

        Set<LocalDate> nightsWithSleep = sessions.stream()
                .flatMap(this::findCoveredNights)
                .collect(Collectors.toSet());

        int sleeplessNights = allNights.size() - nightsWithSleep.size();
        return new SleepAnalysisResult<>(DESCRIPTION, sleeplessNights);
    }

    private LocalDate toNightDate(LocalDateTime dateTime) {
        if (dateTime.getHour() < 12) {
            return dateTime.toLocalDate().minusDays(1);
        }
        return dateTime.toLocalDate();
    }

    private Stream<LocalDate> findCoveredNights(SleepingSession session) {
        LocalDateTime start = session.getStartTime();
        LocalDateTime end = session.getEndTime();
        LocalDate startNight = toNightDate(start).minusDays(1);
        LocalDate endNight = toNightDate(end).plusDays(1);
        return Stream.iterate(startNight, d -> d.plusDays(1))
                .limit(endNight.toEpochDay() - startNight.toEpochDay() + 1)
                .filter(date -> {
                    LocalDateTime nightStart = date.atStartOfDay();
                    LocalDateTime nightEnd = nightStart.plusHours(6);
                    return start.isBefore(nightEnd) && end.isAfter(nightStart);
                });
    }
}
