package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SleeplessNightsCountFunction implements SleepAnalysisFunction<Integer> {

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
        for (LocalDate date = firstNight; !date.isAfter(lastNight); date = date.plusDays(1)) {
            allNights.add(date);
        }

        Set<LocalDate> nightsWithSleep = new TreeSet<>();
        for (SleepingSession session : sessions) {
            LocalDate nightDate = getNightDate(session.getStartTime());
            if (hasNightSleep(session)) {
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

    private boolean hasNightSleep(SleepingSession session) {
        LocalDateTime start = session.getStartTime();
        LocalDateTime end = session.getEndTime();
        LocalDate nightDate = getNightDate(start);
        LocalDateTime nightStart = nightDate.atTime(0, 0);
        LocalDateTime nightEnd = nightDate.atTime(6, 0);
        return start.isBefore(nightEnd) && end.isAfter(nightStart);
    }
}
