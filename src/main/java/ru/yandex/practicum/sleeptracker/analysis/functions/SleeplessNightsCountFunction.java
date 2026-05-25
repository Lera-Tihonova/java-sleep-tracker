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

        Set<LocalDate> allNights = new HashSet<>();
        Set<LocalDate> nightsWithSleep = new HashSet<>();

        System.out.println("=== DEBUG ===");
        for (SleepingSession session : sessions) {
            LocalDateTime start = session.getStartTime();
            LocalDateTime end = session.getEndTime();
            System.out.println("Session: " + start + " -> " + end);

            LocalDate night1 = getNightDate(start);
            LocalDate night2 = night1.plusDays(1);
            System.out.println("  night1: " + night1);
            System.out.println("  night2: " + night2);

            allNights.add(night1);
            allNights.add(night2);

            boolean sleep1 = isSleepAtNight(start, end, night1);
            boolean sleep2 = isSleepAtNight(start, end, night2);
            System.out.println("  covers night1: " + sleep1);
            System.out.println("  covers night2: " + sleep2);

            if (sleep1) nightsWithSleep.add(night1);
            if (sleep2) nightsWithSleep.add(night2);
        }

        System.out.println("All nights: " + allNights);
        System.out.println("Nights with sleep: " + nightsWithSleep);
        System.out.println("Sleepless: " + (allNights.size() - nightsWithSleep.size()));

        int result = allNights.size() - nightsWithSleep.size();
        return new SleepAnalysisResult<>(DESCRIPTION, result);
    }

    private LocalDate getNightDate(LocalDateTime dateTime) {
        if (dateTime.getHour() < 6) {
            return dateTime.toLocalDate().minusDays(1);
        }
        return dateTime.toLocalDate();
    }

    private boolean isSleepAtNight(LocalDateTime start, LocalDateTime end, LocalDate night) {
        LocalDateTime nightStart = night.atStartOfDay();
        LocalDateTime nightEnd = nightStart.plusHours(6);
        return start.isBefore(nightEnd) && end.isAfter(nightStart);
    }
}
