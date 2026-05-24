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
        if (sessions.isEmpty()) return new SleepAnalysisResult<>(DESCRIPTION, 0);

        LocalDateTime firstStart = sessions.get(0).getStartTime();
        LocalDateTime lastEnd = sessions.get(0).getEndTime();
        for (SleepingSession s : sessions) {
            if (s.getStartTime().isBefore(firstStart)) firstStart = s.getStartTime();
            if (s.getEndTime().isAfter(lastEnd)) lastEnd = s.getEndTime();
        }

        LocalDate firstNight = firstStart.toLocalDate();
        if (firstStart.getHour() < 12) firstNight = firstNight.minusDays(1);
        LocalDate lastNight = lastEnd.toLocalDate();
        if (lastEnd.getHour() < 12) lastNight = lastNight.minusDays(1);

        Set<LocalDate> allNights = new HashSet<>();
        for (LocalDate d = firstNight; !d.isAfter(lastNight); d = d.plusDays(1)) allNights.add(d);

        Set<LocalDate> nightsWithSleep = new HashSet<>();
        for (SleepingSession s : sessions) {
            LocalDateTime start = s.getStartTime();
            LocalDateTime end = s.getEndTime();
            LocalDateTime nightStart = start.toLocalDate().atStartOfDay();
            LocalDateTime nightEnd = nightStart.plusHours(6);
            if (start.isBefore(nightEnd) && end.isAfter(nightStart)) {
                LocalDate night = start.toLocalDate();
                if (start.getHour() < 12) night = night.minusDays(1);
                nightsWithSleep.add(night);
            }
        }

        return new SleepAnalysisResult<>(DESCRIPTION, allNights.size() - nightsWithSleep.size());
    }
}
