package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@SuppressWarnings("unused")
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

            LocalDate night = start.toLocalDate();
            if (start.getHour() < 6) {
                night = night.minusDays(1);
            }

            if (coversNight(start, end, night)) {
                nightsWithSleep.add(night);
            } else if (coversNight(start, end, night.plusDays(1))) {
                nightsWithSleep.add(night.plusDays(1));
            }
        }

        if (nightsWithSleep.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, 0);
        }

        LocalDate firstNight = nightsWithSleep.stream().min(LocalDate::compareTo).get();
        LocalDate lastNight = nightsWithSleep.stream().max(LocalDate::compareTo).get();

        int totalNights = (int) (lastNight.toEpochDay() - firstNight.toEpochDay() + 1);
        int sleeplessNights = totalNights - nightsWithSleep.size();

        return new SleepAnalysisResult<>(DESCRIPTION, sleeplessNights);
    }

    private boolean coversNight(LocalDateTime start, LocalDateTime end, LocalDate night) {
        LocalDateTime nightStart = night.atStartOfDay();
        LocalDateTime nightEnd = nightStart.plusHours(6);
        return start.isBefore(nightEnd) && end.isAfter(nightStart);
    }
}
