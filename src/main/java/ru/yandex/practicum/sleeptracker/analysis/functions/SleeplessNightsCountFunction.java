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

        Set<LocalDate> allNights = new HashSet<>();
        Set<LocalDate> nightsWithSleep = new HashSet<>();

        for (SleepingSession session : sessions) {
            LocalDateTime start = session.getStartTime();
            LocalDateTime end = session.getEndTime();

            LocalDate night = getNight(start);
            if (coversNight(start, end, night)) {
                nightsWithSleep.add(night);
                allNights.add(night);
            } else {
                LocalDate nextNight = night.plusDays(1);
                if (coversNight(start, end, nextNight)) {
                    nightsWithSleep.add(nextNight);
                    allNights.add(nextNight);
                }
            }
        }

        if (!nightsWithSleep.isEmpty()) {
            LocalDate first = nightsWithSleep.stream().min(LocalDate::compareTo).get();
            LocalDate last = nightsWithSleep.stream().max(LocalDate::compareTo).get();
            for (LocalDate date = first; !date.isAfter(last); date = date.plusDays(1)) {
                allNights.add(date);
            }
        }

        return new SleepAnalysisResult<>(DESCRIPTION, allNights.size() - nightsWithSleep.size());
    }

    private LocalDate getNight(LocalDateTime dateTime) {
        int hour = dateTime.getHour();
        if (hour < 6) {
            return dateTime.toLocalDate().minusDays(1);
        }
        return dateTime.toLocalDate();
    }

    private boolean coversNight(LocalDateTime start, LocalDateTime end, LocalDate night) {
        LocalDateTime nightStart = night.atStartOfDay();
        LocalDateTime nightEnd = nightStart.plusHours(6);
        return start.isBefore(nightEnd) && end.isAfter(nightStart);
    }
}
