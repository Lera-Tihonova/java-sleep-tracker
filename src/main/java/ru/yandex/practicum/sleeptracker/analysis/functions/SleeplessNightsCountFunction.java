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
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, 0);
        }

        Set<LocalDate> allNights = new HashSet<>();
        Set<LocalDate> nightsWithSleep = new HashSet<>();

        for (SleepingSession session : sessions) {
            LocalDateTime start = session.getStartTime();
            LocalDateTime end = session.getEndTime();

            LocalDate night = start.toLocalDate();
            if (start.toLocalTime().isBefore(LocalTime.of(12, 0))) {
                night = night.minusDays(1);
            }

            allNights.add(night);
            allNights.add(night.plusDays(1));

            LocalDateTime nightStart = night.atStartOfDay();
            LocalDateTime nightEnd = nightStart.plusHours(6);
            if (start.isBefore(nightEnd) && end.isAfter(nightStart)) {
                nightsWithSleep.add(night);
            }

            LocalDateTime nextNightStart = night.plusDays(1).atStartOfDay();
            LocalDateTime nextNightEnd = nextNightStart.plusHours(6);
            if (start.isBefore(nextNightEnd) && end.isAfter(nextNightStart)) {
                nightsWithSleep.add(night.plusDays(1));
            }
        }

        int sleepless = allNights.size() - nightsWithSleep.size();
        return new SleepAnalysisResult<>(DESCRIPTION, sleepless);
    }
}
