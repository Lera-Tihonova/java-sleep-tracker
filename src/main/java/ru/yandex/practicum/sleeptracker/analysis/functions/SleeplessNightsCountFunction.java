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
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, 0);
        }

        Set<LocalDate> nightsWithSleep = new HashSet<>();

        for (SleepingSession session : sessions) {
            LocalDateTime start = session.getStartTime();
            LocalDateTime end = session.getEndTime();

            LocalDate night = start.toLocalDate();
            if (start.getHour() >= 18 || (start.getHour() < 6 && end.getHour() > 0)) {
                night = night.plusDays(1);
            }
            if (start.getHour() < 6) {
                night = night.minusDays(1);
            }

            if (coversNight(start, end, night)) {
                nightsWithSleep.add(night);
            }
        }

        Set<LocalDate> allNights = new HashSet<>();
        if (!nightsWithSleep.isEmpty()) {
            LocalDate first = nightsWithSleep.stream().min(LocalDate::compareTo).get();
            LocalDate last = nightsWithSleep.stream().max(LocalDate::compareTo).get();
            for (LocalDate date = first; !date.isAfter(last); date = date.plusDays(1)) {
                allNights.add(date);
            }
        } else {
            LocalDate first = getNight(sessions.get(0).getStartTime());
            LocalDate last = getNight(sessions.get(sessions.size() - 1).getEndTime());
            for (LocalDate date = first; !date.isAfter(last); date = date.plusDays(1)) {
                allNights.add(date);
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

    private boolean coversNight(LocalDateTime start, LocalDateTime end, LocalDate night) {
        LocalDateTime nightStart = night.atStartOfDay();
        LocalDateTime nightEnd = nightStart.plusHours(6);
        return start.isBefore(nightEnd) && end.isAfter(nightStart);
    }
}
