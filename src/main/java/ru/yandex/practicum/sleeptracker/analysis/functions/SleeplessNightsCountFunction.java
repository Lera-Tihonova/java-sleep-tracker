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

        // 1. Определяем ночи, в которые был сон
        for (SleepingSession session : sessions) {
            LocalDateTime start = session.getStartTime();
            LocalDateTime end = session.getEndTime();

            // Ночь начинается в 00:00 и заканчивается в 06:00.
            // Для сессии проверяем две возможные ночи: текущую (по дате старта) и следующую.
            LocalDate currentNight = start.toLocalDate();
            // Если сессия началась после полуночи (час < 6), она принадлежит предыдущей ночи
            if (start.getHour() < 6) {
                currentNight = currentNight.minusDays(1);
            }

            checkAndAddNight(start, end, currentNight, nightsWithSleep);
            checkAndAddNight(start, end, currentNight.plusDays(1), nightsWithSleep);
        }

        // 2. Если нет ни одной ночи со сном (все сессии дневные)
        if (nightsWithSleep.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, 0);
        }

        // 3. Определяем диапазон всех ночей от первой до последней, где был сон
        LocalDate firstNight = nightsWithSleep.stream().min(LocalDate::compareTo).get();
        LocalDate lastNight = nightsWithSleep.stream().max(LocalDate::compareTo).get();

        long totalNights = firstNight.until(lastNight, java.time.temporal.ChronoUnit.DAYS) + 1;
        int sleeplessNights = (int) (totalNights - nightsWithSleep.size());

        return new SleepAnalysisResult<>(DESCRIPTION, sleeplessNights);
    }

    private void checkAndAddNight(LocalDateTime start, LocalDateTime end, LocalDate night, Set<LocalDate> nightsWithSleep) {
        LocalDateTime nightStart = night.atStartOfDay();
        LocalDateTime nightEnd = nightStart.plusHours(6);
        if (start.isBefore(nightEnd) && end.isAfter(nightStart)) {
            nightsWithSleep.add(night);
        }
    }
}