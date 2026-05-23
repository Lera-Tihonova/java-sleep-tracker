package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleeplessNightsCountFunction implements SleepAnalysisFunction<Integer> {

    private static final LocalTime NOON = LocalTime.of(12, 0);

    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Количество бессонных ночей", 0);
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

        Set<LocalDate> allNights = Stream.iterate(firstNight, date -> date.plusDays(1))
            .limit(Period.between(firstNight, lastNight).getDays() + 1)
            .collect(Collectors.toSet());

        Set<LocalDate> nightsWithSleep = sessions.stream()
            .filter(SleepingSession::isNightSession)
            .flatMap(session -> getNightsForSession(session))
            .collect(Collectors.toSet());

        long sleeplessNights = allNights.size() - nightsWithSleep.size();

        return new SleepAnalysisResult<>("Количество бессонных ночей", (int) sleeplessNights);
    }

    private LocalDate getNightDate(LocalDateTime dateTime) {
        if (dateTime.toLocalTime().isBefore(NOON)) {
            return dateTime.toLocalDate().minusDays(1);
        } else {
            return dateTime.toLocalDate();
        }
    }

    private Stream<LocalDate> getNightsForSession(SleepingSession session) {
        LocalDate startNight = getNightDate(session.getStartTime());
        LocalDate endNight = getNightDate(session.getEndTime());
        return Stream.iterate(startNight, date -> date.plusDays(1))
            .limit(Period.between(startNight, endNight).getDays() + 1);
    }
}
