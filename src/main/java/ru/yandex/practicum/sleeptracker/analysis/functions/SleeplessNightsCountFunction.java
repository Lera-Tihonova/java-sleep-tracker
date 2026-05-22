package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleeplessNightsCountFunction implements SleepAnalysisFunction<Integer> {

    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Количество бессонных ночей", 0);
        }

        LocalDateTime firstSessionStart = sessions.stream()
            .map(SleepingSession::getStartTime)
            .min(LocalDateTime::compareTo)
            .orElse(null);

        LocalDateTime lastSessionEnd = sessions.stream()
            .map(SleepingSession::getEndTime)
            .max(LocalDateTime::compareTo)
            .orElse(null);

        if (firstSessionStart == null || lastSessionEnd == null) {
            return new SleepAnalysisResult<>("Количество бессонных ночей", 0);
        }

        LocalDate startDate = getNightDate(firstSessionStart);
        LocalDate endDate = getNightDate(lastSessionEnd);

        Set<LocalDate> nightsWithSleep = sessions.stream()
            .filter(SleepingSession::isNightSession)
            .flatMap(session -> getNightDatesForSession(session))
            .collect(Collectors.toSet());

        long totalNights = Period.between(startDate, endDate).getDays() + 1;
        long sleeplessNights = totalNights - nightsWithSleep.size();

        return new SleepAnalysisResult<>("Количество бессонных ночей", (int) sleeplessNights);
    }

    private LocalDate getNightDate(LocalDateTime dateTime) {
        LocalDateTime noon = dateTime.toLocalDate().atTime(12, 0);
        if (dateTime.isBefore(noon)) {
            return dateTime.toLocalDate().minusDays(1);
        } else {
            return dateTime.toLocalDate();
        }
    }

    private Stream<LocalDate> getNightDatesForSession(SleepingSession session) {
        return Stream.iterate(session.getStartTime().toLocalDate(), date -> date.plusDays(1))
            .limit(java.time.temporal.ChronoUnit.DAYS.between(
                session.getStartTime().toLocalDate(),
                session.getEndTime().toLocalDate()) + 1)
            .filter(date -> {
                LocalDateTime checkNightStart = date.atStartOfDay();
                LocalDateTime checkNightEnd = checkNightStart.plusHours(6);
                return session.getStartTime().isBefore(checkNightEnd) &&
                       session.getEndTime().isAfter(checkNightStart);
            });
    }
}
