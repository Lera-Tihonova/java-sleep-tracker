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

        LocalDateTime firstStart = sessions.get(0).getStartTime();
        LocalDateTime lastEnd = sessions.get(0).getEndTime();

        for (SleepingSession session : sessions) {
            if (session.getStartTime().isBefore(firstStart)) {
                firstStart = session.getStartTime();
            }
            if (session.getEndTime().isAfter(lastEnd)) {
                lastEnd = session.getEndTime();
            }
        }

        LocalDate firstNight = firstStart.toLocalDate();
        if (firstStart.getHour() < 12) {
            firstNight = firstNight.minusDays(1);
        }

        LocalDate lastNight = lastEnd.toLocalDate();
        if (lastEnd.getHour() < 12) {
            lastNight = lastNight.minusDays(1);
        }

        Set<LocalDate> allNights = new HashSet<>();
        for (LocalDate date = firstNight; !date.isAfter(lastNight); date = date.plusDays(1)) {
            allNights.add(date);
        }

        Set<LocalDate> nightsWithSleep = new HashSet<>();
        for (SleepingSession session : sessions) {
            if (session.isNightSession()) {
                LocalDate night = session.getStartTime().toLocalDate();
                if (session.getStartTime().getHour() < 12) {
                    night = night.minusDays(1);
                }
                nightsWithSleep.add(night);
            }
        }

        int sleeplessNights = allNights.size() - nightsWithSleep.size();
        return new SleepAnalysisResult<>(DESCRIPTION, sleeplessNights);
    }
}
