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
    private static final int NIGHT_END_HOUR = 6;

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
            if (start.toLocalTime().isBefore(LocalTime.of(6, 0))) {
                night = night.minusDays(1);
            }

            LocalDateTime nightStart = night.atStartOfDay();
            LocalDateTime nightEnd = nightStart.plusHours(NIGHT_END_HOUR);

            if (start.isBefore(nightEnd) && end.isAfter(nightStart)) {
                nightsWithSleep.add(night);
            }

            LocalDate nextNight = night.plusDays(1);
            LocalDateTime nextNightStart = nextNight.atStartOfDay();
            LocalDateTime nextNightEnd = nextNightStart.plusHours(NIGHT_END_HOUR);

            if (start.isBefore(nextNightEnd) && end.isAfter(nextNightStart)) {
                nightsWithSleep.add(nextNight);
            }
        }

        if (nightsWithSleep.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, 0);
        }

        LocalDate first = nightsWithSleep.stream().min(LocalDate::compareTo).get();
        LocalDate last = nightsWithSleep.stream().max(LocalDate::compareTo).get();

        long totalDays = last.toEpochDay() - first.toEpochDay() + 1;
        int sleepless = (int) (totalDays - nightsWithSleep.size());

        return new SleepAnalysisResult<>(DESCRIPTION, sleepless);
    }
}
