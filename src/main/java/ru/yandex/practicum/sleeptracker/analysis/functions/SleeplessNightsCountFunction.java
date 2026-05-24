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
    private static final String DESCRIPTION = "Количество бессонных ночей";

    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, 0);
        }

        Set<LocalDate> nightsWithSleep = new HashSet<>();

        for (SleepingSession session : sessions) {
            LocalDateTime start = session.getStartTime();
            LocalDateTime end = session.getEndTime();

            LocalDate nightDate = getNightDate(start);
            LocalDateTime nightStart = nightDate.atStartOfDay();
            LocalDateTime nightEnd = nightStart.plusHours(6);

            if (start.isBefore(nightEnd) && end.isAfter(nightStart)) {
                nightsWithSleep.add(nightDate);
            }
        }

        if (nightsWithSleep.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, 0);
        }

        LocalDate firstNight = nightsWithSleep.stream().min(LocalDate::compareTo).get();
        LocalDate lastNight = nightsWithSleep.stream().max(LocalDate::compareTo).get();

        long totalNights = lastNight.toEpochDay() - firstNight.toEpochDay() + 1;
        int sleeplessNights = (int) (totalNights - nightsWithSleep.size());

        return new SleepAnalysisResult<>(DESCRIPTION, sleeplessNights);
    }

    private LocalDate getNightDate(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();
        if (time.isBefore(LocalTime.of(12, 0))) {
            return dateTime.toLocalDate().minusDays(1);
        }
        return dateTime.toLocalDate();
    }
}
