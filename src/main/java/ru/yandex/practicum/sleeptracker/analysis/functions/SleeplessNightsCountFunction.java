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
            LocalDate night = getNight(s.getStartTime());
            if (firstNight == null || night.isBefore(firstNight)) firstNight = night;
            if (lastNight == null || night.isAfter(lastNight)) lastNight = night;
        }

        Set<LocalDate> allNights = new HashSet<>();
        for (LocalDate d = firstNight; !d.isAfter(lastNight); d = d.plusDays(1)) {
            allNights.add(d);
        }

        Set<LocalDate> nightsWithSleep = new HashSet<>();
        for (SleepingSession s : sessions) {
            LocalDateTime start = s.getStartTime();
            LocalDateTime end = s.getEndTime();
            LocalDate night = getNight(start);
            if (isNightSleep(start, end, night)) {
                nightsWithSleep.add(night);
            }
        }

        return new SleepAnalysisResult<>(DESCRIPTION, allNights.size() - nightsWithSleep.size());
    }

    private LocalDate getNight(LocalDateTime dateTime) {
        if (dateTime.getHour() < 12) {
            return dateTime.toLocalDate().minusDays(1);
        }
        return dateTime.toLocalDate();
    }

    private boolean isNightSleep(LocalDateTime start, LocalDateTime end, LocalDate night) {
        LocalDateTime nightStart = night.atStartOfDay();
        LocalDateTime nightEnd = nightStart.plusHours(6);
        return start.isBefore(nightEnd) && end.isAfter(nightStart);
    }
}
