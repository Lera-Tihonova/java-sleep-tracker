package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class SleeplessNightsCountFunction implements SleepAnalysisFunction<Integer> {
    private static final String DESCRIPTION = "Количество бессонных ночей";

    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, 0);
        }

        LocalDate firstNight = null;
        LocalDate lastNight = null;

        for (SleepingSession session : sessions) {
            LocalDate night = getNightDate(session.getStartTime());
            if (firstNight == null || night.isBefore(firstNight)) {
                firstNight = night;
            }
            if (lastNight == null || night.isAfter(lastNight)) {
                lastNight = night;
            }
        }

        Set<LocalDate> allNights = new HashSet<>();
        for (LocalDate date = firstNight; !date.isAfter(lastNight); date = date.plusDays(1)) {
            allNights.add(date);
        }

        Set<LocalDate> nightsWithSleep = new HashSet<>();
        for (SleepingSession session : sessions) {
            LocalDateTime start = session.getStartTime();
            LocalDateTime end = session.getEndTime();
            LocalDate night = getNightDate(start);
            LocalDateTime nightStart = night.atStartOfDay();
            LocalDateTime nightEnd = nightStart.plusHours(6);
            if (start.isBefore(nightEnd) && end.isAfter(nightStart)) {
                nightsWithSleep.add(night);
            }
        }

        int sleepless = allNights.size() - nightsWithSleep.size();
        return new SleepAnalysisResult<>(DESCRIPTION, sleepless);
    }

    private LocalDate getNightDate(LocalDateTime dateTime) {
        if (dateTime.getHour() < 12) {
            return dateTime.toLocalDate().minusDays(1);
        }
        return dateTime.toLocalDate();
    }
}
