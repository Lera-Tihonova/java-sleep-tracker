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

        for (SleepingSession s : sessions) {
            LocalDateTime start = s.getStartTime();
            LocalDateTime end = s.getEndTime();

            LocalDate night = start.toLocalDate();
            if (start.getHour() < 12) {
                night = night.minusDays(1);
            }

            if (firstNight == null || night.isBefore(firstNight)) {
                firstNight = night;
            }
            if (lastNight == null || night.isAfter(lastNight)) {
                lastNight = night;
            }

            LocalDate nightEnd = end.toLocalDate();
            if (end.getHour() < 12) {
                nightEnd = nightEnd.minusDays(1);
            }
            if (nightEnd.isAfter(lastNight)) {
                lastNight = nightEnd;
            }
            if (nightEnd.isBefore(firstNight)) {
                firstNight = nightEnd;
            }
        }

        Set<LocalDate> allNights = new HashSet<>();
        for (LocalDate d = firstNight; !d.isAfter(lastNight); d = d.plusDays(1)) {
            allNights.add(d);
        }

        Set<LocalDate> nightsWithSleep = new HashSet<>();
        for (SleepingSession s : sessions) {
            LocalDateTime start = s.getStartTime();
            LocalDateTime end = s.getEndTime();

            LocalDate night = start.toLocalDate();
            if (start.getHour() < 12) {
                night = night.minusDays(1);
            }

            LocalDateTime nightStart = night.atStartOfDay();
            LocalDateTime nightEnd = nightStart.plusHours(6);

            if (start.isBefore(nightEnd) && end.isAfter(nightStart)) {
                nightsWithSleep.add(night);
            }

            LocalDate nextNight = night.plusDays(1);
            LocalDateTime nextNightStart = nextNight.atStartOfDay();
            LocalDateTime nextNightEnd = nextNightStart.plusHours(6);
            if (start.isBefore(nextNightEnd) && end.isAfter(nextNightStart)) {
                nightsWithSleep.add(nextNight);
            }
        }

        return new SleepAnalysisResult<>(DESCRIPTION, allNights.size() - nightsWithSleep.size());
    }
}
