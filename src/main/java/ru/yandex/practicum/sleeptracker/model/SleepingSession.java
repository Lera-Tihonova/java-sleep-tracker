package ru.yandex.practicum.sleeptracker.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.LocalTime;

public class SleepingSession {

    private static final LocalTime NIGHT_START = LocalTime.of(0, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final SleepQuality quality;

    public enum SleepQuality {
        GOOD, NORMAL, BAD
    }

    public SleepingSession(LocalDateTime startTime, LocalDateTime endTime, SleepQuality quality) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.quality = quality;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public long getDurationMinutes() {
        return Duration.between(startTime, endTime).toMinutes();
    }

    public boolean isNightSession() {
        // Сессия считается ночной, если она пересекается с интервалом 0:00 - 6:00 любого дня
        LocalDateTime currentNightStart = startTime.toLocalDate().atTime(NIGHT_START);
        LocalDateTime currentNightEnd = startTime.toLocalDate().atTime(NIGHT_END);

        // Проверяем текущую ночь
        if (startTime.isBefore(currentNightEnd) && endTime.isAfter(currentNightStart)) {
            return true;
        }

        // Проверяем следующую ночь (если сессия длится несколько дней)
        LocalDateTime nextNightStart = startTime.toLocalDate().plusDays(1).atTime(NIGHT_START);
        LocalDateTime nextNightEnd = startTime.toLocalDate().plusDays(1).atTime(NIGHT_END);

        return startTime.isBefore(nextNightEnd) && endTime.isAfter(nextNightStart);
    }
}
