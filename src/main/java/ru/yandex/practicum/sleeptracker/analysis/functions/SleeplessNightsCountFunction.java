package ru.yandex.practicum.sleeptracker.analysis.functions;

import ru.yandex.practicum.sleeptracker.analysis.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class SleeplessNightsCountFunction implements SleepAnalysisFunction<Integer> {

    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Количество бессонных ночей", 0);
        }

        LocalDateTime firstStart = findFirstStart(sessions);
        LocalDateTime lastEnd = findLastEnd(sessions);

        LocalDate firstNight = convertToNight(firstStart);
        LocalDate lastNight = convertToNight(lastEnd);

        Set<LocalDate> allNights = generateAllNights(firstNight, lastNight);
        Set<LocalDate> nightsWithSleep = findNightsWithSleep(sessions);

        int sleepless = allNights.size() - nightsWithSleep.size();
        return new SleepAnalysisResult<>("Количество бессонных ночей", sleepless);
    }

    private LocalDateTime findFirstStart(List<SleepingSession> sessions) {
        return sessions.stream()
            .map(SleepingSession::getStartTime)
            .min(LocalDateTime::compareTo)
            .get();
    }

    private LocalDateTime findLastEnd(List<SleepingSession> sessions) {
        return sessions.stream()
            .map(SleepingSession::getEndTime)
            .max(LocalDateTime::compareTo)
            .get();
    }

    private LocalDate convertToNight(LocalDateTime dateTime) {
        if (dateTime.getHour() < 12) {
            return dateTime.toLocalDate().minusDays(1);
        }
        return dateTime.toLocalDate();
    }

    private Set<LocalDate> generateAllNights(LocalDate first, LocalDate last) {
        Set<LocalDate> nights = new HashSet<>();
        LocalDate current = first;
        while (!current.isAfter(last)) {
            nights.add(current);
            current = current.plusDays(1);
        }
        return nights;
    }

    private Set<LocalDate> findNightsWithSleep(List<SleepingSession> sessions) {
        Set<LocalDate> nights = new HashSet<>();
        for (SleepingSession session : sessions) {
            LocalDateTime start = session.getStartTime();
            LocalDateTime end = session.getEndTime();

            LocalDate nightDate = convertToNight(start);
            LocalDateTime nightStart = nightDate.atTime(0, 0);
            LocalDateTime nightEnd = nightDate.atTime(6, 0);

            boolean coversNight = start.isBefore(nightEnd) && end.isAfter(nightStart);
            if (coversNight) {
                nights.add(nightDate);
            }
        }
        return nights;
    }
}
