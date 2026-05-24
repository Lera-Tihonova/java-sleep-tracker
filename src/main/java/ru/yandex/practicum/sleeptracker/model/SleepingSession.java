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
        LocalDateTime nightStartToday = startTime.toLocalDate().atStartOfDay();
        LocalDateTime nightEndToday = nightStartToday.plusHours(6);
        if (startTime.isBefore(nightEndToday) && endTime.isAfter(nightStartToday)) {
            return true;
        }
        LocalDateTime nightStartTomorrow = startTime.toLocalDate().plusDays(1).atStartOfDay();
        LocalDateTime nightEndTomorrow = nightStartTomorrow.plusHours(6);
        return startTime.isBefore(nightEndTomorrow) && endTime.isAfter(nightStartTomorrow);
    }
}
