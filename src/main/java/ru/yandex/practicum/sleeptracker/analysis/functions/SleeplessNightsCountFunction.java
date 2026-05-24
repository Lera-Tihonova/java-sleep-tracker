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

        LocalDateTime firstStart = sessions.stream()
                .map(SleepingSession::getStartTime)
                .min(LocalDateTime::compareTo)
                .get();

        LocalDateTime lastEnd = sessions.stream()
                .map(SleepingSession::getEndTime)
                .max(LocalDateTime::compareTo)
                .get();

        LocalDate firstNight = getNightDate(firstStart);
        LocalDate lastNight = getNightDate(lastEnd);

        Set<LocalDate> allNights = new HashSet<>();
        for (LocalDate date = firstNight; !date.isAfter(lastNight); date = date.plusDays(1)) {
            allNights.add(date);
        }

        Set<LocalDate> nightsWithSleep = new HashSet<>();
        for (SleepingSession session : sessions) {
            nightsWithSleep.addAll(findNightsCoveredBySession(session));
        }

        int sleeplessNights = allNights.size() - nightsWithSleep.size();
        return new SleepAnalysisResult<>(DESCRIPTION, sleeplessNights);
    }

    private LocalDate getNightDate(LocalDateTime dateTime) {
        if (dateTime.getHour() < 12) {
            return dateTime.toLocalDate().minusDays(1);
        }
        return dateTime.toLocalDate();
    }

    private Set<LocalDate> findNightsCoveredBySession(SleepingSession session) {
        Set<LocalDate> nights = new HashSet<>();
        LocalDateTime start = session.getStartTime();
        LocalDateTime end = session.getEndTime();
        LocalDate startNight = getNightDate(start);
        LocalDate endNight = getNightDate(end);

        for (LocalDate night = startNight.minusDays(1); !night.isAfter(endNight.plusDays(1)); night = night.plusDays(1)) {
            LocalDateTime nightStart = night.atStartOfDay();
            LocalDateTime nightEnd = nightStart.plusHours(6);
            if (start.isBefore(nightEnd) && end.isAfter(nightStart)) {
                nights.add(night);
            }
        }
        return nights;
    }
}
