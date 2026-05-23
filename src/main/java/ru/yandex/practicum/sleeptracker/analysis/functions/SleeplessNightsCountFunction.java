package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class SleeplessNightsCountFunction implements SleepAnalysisFunction<Integer> {

    private static final LocalTime NIGHT_START = LocalTime.of(0, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

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

        Set<LocalDate> allNights = new TreeSet<>();
        LocalDate current = firstNight;
        while (!current.isAfter(lastNight)) {
            allNights.add(current);
            current = current.plusDays(1);
        }

        Set<LocalDate> nightsWithSleep = new TreeSet<>();
        for (SleepingSession session : sessions) {
            if (coversNight(session)) {
                LocalDate nightDate = getSessionNightDate(session);
                nightsWithSleep.add(nightDate);
            }
        }

        int sleeplessNights = allNights.size() - nightsWithSleep.size();
        return new SleepAnalysisResult<>("Количество бессонных ночей", sleeplessNights);
    }

    private LocalDate getNightDate(LocalDateTime dateTime) {
        if (dateTime.toLocalTime().isBefore(LocalTime.of(12, 0))) {
            return dateTime.toLocalDate().minusDays(1);
        }
        return dateTime.toLocalDate();
    }

    private boolean coversNight(SleepingSession session) {
        LocalDateTime sessionStart = session.getStartTime();
        LocalDateTime sessionEnd = session.getEndTime();
        LocalDateTime nightStartForDay = sessionStart.toLocalDate().atTime(NIGHT_START);
        LocalDateTime nightEndForDay = sessionStart.toLocalDate().atTime(NIGHT_END);
        return sessionStart.isBefore(nightEndForDay) && sessionEnd.isAfter(nightStartForDay);
    }

    private LocalDate getSessionNightDate(SleepingSession session) {
        LocalDateTime sessionStart = session.getStartTime();
        LocalTime startTime = sessionStart.toLocalTime();
        if (startTime.isBefore(NIGHT_END) || startTime.equals(NIGHT_START)) {
            return sessionStart.toLocalDate().minusDays(1);
        }
        return sessionStart.toLocalDate();
    }
}
