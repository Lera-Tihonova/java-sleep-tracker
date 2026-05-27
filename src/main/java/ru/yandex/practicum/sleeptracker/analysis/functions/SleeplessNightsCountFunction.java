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
        Set<LocalDate> allNights = new HashSet<>();

        for (SleepingSession session : sessions) {
            LocalDateTime start = session.getStartTime();
            LocalDateTime end = session.getEndTime();

            LocalDate startNight = start.toLocalDate();
            if (start.toLocalTime().isBefore(LocalTime.of(6, 0))) {
                startNight = startNight.minusDays(1);
            }

            LocalDate endNight = end.toLocalDate();
            if (end.toLocalTime().isBefore(LocalTime.of(6, 0))) {
                endNight = endNight.minusDays(1);
            }

            for (LocalDate night = startNight; !night.isAfter(endNight); night = night.plusDays(1)) {
                allNights.add(night);
                if (isNightSleep(start, end, night)) {
                    nightsWithSleep.add(night);
                }
            }
        }

        int sleepless = allNights.size() - nightsWithSleep.size();
        return new SleepAnalysisResult<>(DESCRIPTION, sleepless);
    }

    private boolean isNightSleep(LocalDateTime start, LocalDateTime end, LocalDate night) {
        LocalDateTime nightStart = night.atStartOfDay();
        LocalDateTime nightEnd = nightStart.plusHours(6);
        return start.isBefore(nightEnd) && end.isAfter(nightStart);
    }
}
