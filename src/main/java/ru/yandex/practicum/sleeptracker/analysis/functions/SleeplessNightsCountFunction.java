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

        System.out.println("=== DEBUG SleeplessNightsCountFunction ===");

        Set<LocalDate> nightsWithSleep = new HashSet<>();

        for (SleepingSession session : sessions) {
            LocalDateTime start = session.getStartTime();
            LocalDateTime end = session.getEndTime();

            System.out.println("Session: " + start + " -> " + end);

            LocalDate night = start.toLocalDate();
            if (start.toLocalTime().isBefore(LocalTime.of(6, 0))) {
                night = night.minusDays(1);
                System.out.println("  start before 6, night adjusted to: " + night);
            } else {
                System.out.println("  start after 6, night: " + night);
            }

            LocalDateTime nightStart = night.atStartOfDay();
            LocalDateTime nightEnd = nightStart.plusHours(6);

            boolean covers = start.isBefore(nightEnd) && end.isAfter(nightStart);
            System.out.println("  covers night " + night + ": " + covers);

            if (covers) {
                nightsWithSleep.add(night);
            }

            LocalDate nextNight = night.plusDays(1);
            LocalDateTime nextNightStart = nextNight.atStartOfDay();
            LocalDateTime nextNightEnd = nextNightStart.plusHours(6);

            boolean coversNext = start.isBefore(nextNightEnd) && end.isAfter(nextNightStart);
            System.out.println("  covers next night " + nextNight + ": " + coversNext);

            if (coversNext) {
                nightsWithSleep.add(nextNight);
            }
        }

        System.out.println("Nights with sleep: " + nightsWithSleep);

        if (nightsWithSleep.isEmpty()) {
            System.out.println("No nights with sleep, returning 0");
            return new SleepAnalysisResult<>(DESCRIPTION, 0);
        }

        LocalDate first = nightsWithSleep.stream().min(LocalDate::compareTo).get();
        LocalDate last = nightsWithSleep.stream().max(LocalDate::compareTo).get();
        System.out.println("First night: " + first + ", Last night: " + last);

        long totalDays = last.toEpochDay() - first.toEpochDay() + 1;
        int sleepless = (int) (totalDays - nightsWithSleep.size());
        System.out.println("Total nights: " + totalDays + ", Nights with sleep: " + nightsWithSleep.size() + ", Sleepless: " + sleepless);

        return new SleepAnalysisResult<>(DESCRIPTION, sleepless);
    }
}
