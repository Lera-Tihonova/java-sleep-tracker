package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class SleeplessNightsCountFunction implements SleepAnalysisFunction<Integer> {

    private static final LocalTime NIGHT_START = LocalTime.of(0, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);
    private static final LocalTime NOON = LocalTime.of(12, 0);

    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Количество бессонных ночей", 0);
        }

        LocalDateTime firstStart = findFirstStart(sessions);
        LocalDateTime lastEnd = findLastEnd(sessions);

        LocalDate firstNight = convertToNightDate(firstStart);
        LocalDate lastNight = convertToNightDate(lastEnd);

        Set<LocalDate> allNights = getAllDatesBetween(firstNight, lastNight);
        Set<LocalDate> nightsWithSleep = getNightsWithSleep(sessions);

        int sleeplessNights = allNights.size() - nightsWithSleep.size();
        return new SleepAnalysisResult<>("Количество бессонных ночей", sleeplessNights);
    }

    private LocalDateTime findFirstStart(List<SleepingSession> sessions) {
        return sessions.stream()
            .map(SleepingSession::getStartTime)
            .min(LocalDateTime::compareTo)
            .get();
    }

    private LocalDateTime findLastEnd(List<SleepingSession> sessions) {
        return sessions.stream()
            .map(SleepingSession::getEndTime)
            .max(LocalDateTime::compareTo)
            .get();
    }

    private LocalDate convertToNightDate(LocalDateTime dateTime) {
        if (dateTime.toLocalTime().isBefore(NOON)) {
            return dateTime.toLocalDate().minusDays(1);
        }
        return dateTime.toLocalDate();
    }

    private Set<LocalDate> getAllDatesBetween(LocalDate start, LocalDate end) {
        Set<LocalDate> dates = new HashSet<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            dates.add(current);
            current = current.plusDays(1);
        }
        return dates;
    }

    private Set<LocalDate> getNightsWithSleep(List<SleepingSession> sessions) {
        Set<LocalDate> nights = new HashSet<>();
        for (SleepingSession session : sessions) {
            LocalDate nightDate = getNightForSession(session);
            if (nightDate != null) {
                nights.add(nightDate);
            }
        }
        return nights;
    }

    private LocalDate getNightForSession(SleepingSession session) {
        LocalDateTime start = session.getStartTime();
        LocalDateTime end = session.getEndTime();
        LocalDate possibleNight = convertToNightDate(start);

        LocalDateTime nightStart = possibleNight.atTime(NIGHT_START);
        LocalDateTime nightEnd = possibleNight.atTime(NIGHT_END);

        if (start.isBefore(nightEnd) && end.isAfter(nightStart)) {
            return possibleNight;
        }

        LocalDate nextNight = possibleNight.plusDays(1);
        LocalDateTime nextNightStart = nextNight.atTime(NIGHT_START);
        LocalDateTime nextNightEnd = nextNight.atTime(NIGHT_END);

        if (start.isBefore(nextNightEnd) && end.isAfter(nextNightStart)) {
            return nextNight;
        }

        return null;
    }
}
