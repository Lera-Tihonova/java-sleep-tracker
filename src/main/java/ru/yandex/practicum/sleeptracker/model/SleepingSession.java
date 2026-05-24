package ru.yandex.practicum.sleeptracker.model;

import java.time.LocalDateTime;
import java.time.Duration;

public class SleepingSession {
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
        LocalDateTime nightStart = startTime.toLocalDate().atStartOfDay();
        LocalDateTime nightEnd = nightStart.plusHours(6);
        boolean coversThisNight = startTime.isBefore(nightEnd) && endTime.isAfter(nightStart);
        if (coversThisNight) {
            return true;
        }
        LocalDateTime nextNightStart = startTime.toLocalDate().plusDays(1).atStartOfDay();
        LocalDateTime nextNightEnd = nextNightStart.plusHours(6);
        return startTime.isBefore(nextNightEnd) && endTime.isAfter(nextNightStart);
    }
}
