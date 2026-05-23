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

        Set<LocalDate> allNights = new HashSet<>();
        for (LocalDate date = firstNight; !date.isAfter(lastNight); date = date.plusDays(1)) {
            allNights.add(date);
        }

        Set<LocalDate> nightsWithSleep = new HashSet<>();
        for (SleepingSession session : sessions) {
            LocalDate night = getNightOfSession(session);
            if (night != null) {
                nightsWithSleep.add(night);
            }
        }

        int sleeplessNights = allNights.size() - nightsWithSleep.size();
        return new SleepAnalysisResult<>("Количество бессонных ночей", sleeplessNights);
    }

    private LocalDate getNightDate(LocalDateTime dateTime) {
        if (dateTime.toLocalTime().isBefore(NOON)) {
            return dateTime.toLocalDate().minusDays(1);
        }
        return dateTime.toLocalDate();
    }

    private LocalDate getNightOfSession(SleepingSession session) {
        LocalDateTime start = session.getStartTime();
        LocalDateTime end = session.getEndTime();

        LocalDate nightDate = getNightDate(start);
        LocalDateTime nightStart = nightDate.atTime(NIGHT_START);
        LocalDateTime nightEnd = nightDate.atTime(NIGHT_END);

        if (start.isBefore(nightEnd) && end.isAfter(nightStart)) {
            return nightDate;
        }

        return null;
    }
}
