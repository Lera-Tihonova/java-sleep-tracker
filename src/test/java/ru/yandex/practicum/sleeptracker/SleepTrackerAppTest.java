package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.analysis.functions.*;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.SleepingSession.SleepQuality;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerAppTest {

    @Test
    void testSessionCountWithEmptyList() {
        List<SleepingSession> sessions = Collections.emptyList();
        SessionCountFunction function = new SessionCountFunction();
        assertEquals(0, function.apply(sessions).getValue());
    }

    @Test
    void testSessionCountWithSingleSession() {
        List<SleepingSession> sessions = Arrays.asList(
            new SleepingSession(LocalDateTime.of(2024, 1, 1, 22, 0), LocalDateTime.of(2024, 1, 2, 8, 0), SleepQuality.GOOD)
        );
        SessionCountFunction function = new SessionCountFunction();
        assertEquals(1, function.apply(sessions).getValue());
    }

    @Test
    void testSessionCountWithMultipleSessions() {
        List<SleepingSession> sessions = Arrays.asList(
            new SleepingSession(LocalDateTime.of(2024, 1, 1, 22, 0), LocalDateTime.of(2024, 1, 2, 8, 0), SleepQuality.GOOD),
            new SleepingSession(LocalDateTime.of(2024, 1, 2, 23, 0), LocalDateTime.of(2024, 1, 3, 7, 0), SleepQuality.NORMAL),
            new SleepingSession(LocalDateTime.of(2024, 1, 3, 23, 30), LocalDateTime.of(2024, 1, 4, 6, 30), SleepQuality.BAD)
        );
        SessionCountFunction function = new SessionCountFunction();
        assertEquals(3, function.apply(sessions).getValue());
    }

    @Test
    void testMinDurationWithEmptyList() {
        List<SleepingSession> sessions = Collections.emptyList();
        MinDurationFunction function = new MinDurationFunction();
        assertEquals(0L, function.apply(sessions).getValue());
    }

    @Test
    void testMinDurationWithSessions() {
        List<SleepingSession> sessions = Arrays.asList(
            new SleepingSession(LocalDateTime.of(2024, 1, 1, 22, 0), LocalDateTime.of(2024, 1, 2, 8, 0), SleepQuality.GOOD),
            new SleepingSession(LocalDateTime.of(2024, 1, 2, 23, 0), LocalDateTime.of(2024, 1, 3, 5, 0), SleepQuality.NORMAL),
            new SleepingSession(LocalDateTime.of(2024, 1, 3, 0, 0), LocalDateTime.of(2024, 1, 3, 6, 0), SleepQuality.BAD)
        );
        MinDurationFunction function = new MinDurationFunction();
        assertEquals(360L, function.apply(sessions).getValue());
    }

    @Test
    void testMaxDurationWithEmptyList() {
        List<SleepingSession> sessions = Collections.emptyList();
        MaxDurationFunction function = new MaxDurationFunction();
        assertEquals(0L, function.apply(sessions).getValue());
    }

    @Test
    void testMaxDurationWithSessions() {
        List<SleepingSession> sessions = Arrays.asList(
            new SleepingSession(LocalDateTime.of(2024, 1, 1, 22, 0), LocalDateTime.of(2024, 1, 2, 10, 0), SleepQuality.GOOD),
            new SleepingSession(LocalDateTime.of(2024, 1, 2, 23, 0), LocalDateTime.of(2024, 1, 3, 7, 0), SleepQuality.NORMAL),
            new SleepingSession(LocalDateTime.of(2024, 1, 3, 0, 0), LocalDateTime.of(2024, 1, 3, 6, 0), SleepQuality.BAD)
        );
        MaxDurationFunction function = new MaxDurationFunction();
        assertEquals(720L, function.apply(sessions).getValue());
    }

    @Test
    void testAvgDurationWithEmptyList() {
        List<SleepingSession> sessions = Collections.emptyList();
        AvgDurationFunction function = new AvgDurationFunction();
        assertEquals(0.0, function.apply(sessions).getValue());
    }

    @Test
    void testAvgDurationWithSessions() {
        List<SleepingSession> sessions = Arrays.asList(
            new SleepingSession(LocalDateTime.of(2024, 1, 1, 22, 0), LocalDateTime.of(2024, 1, 2, 8, 0), SleepQuality.GOOD),
            new SleepingSession(LocalDateTime.of(2024, 1, 2, 23, 0), LocalDateTime.of(2024, 1, 3, 7, 0), SleepQuality.NORMAL)
        );
        AvgDurationFunction function = new AvgDurationFunction();
        assertEquals(540.0, function.apply(sessions).getValue());
    }

    @Test
    void testBadQualityCountWithEmptyList() {
        List<SleepingSession> sessions = Collections.emptyList();
        BadQualityCountFunction function = new BadQualityCountFunction();
        assertEquals(0, function.apply(sessions).getValue());
    }

    @Test
    void testBadQualityCountWithSessions() {
        List<SleepingSession> sessions = Arrays.asList(
            new SleepingSession(LocalDateTime.of(2024, 1, 1, 22, 0), LocalDateTime.of(2024, 1, 2, 8, 0), SleepQuality.GOOD),
            new SleepingSession(LocalDateTime.of(2024, 1, 2, 23, 0), LocalDateTime.of(2024, 1, 3, 7, 0), SleepQuality.BAD),
            new SleepingSession(LocalDateTime.of(2024, 1, 3, 23, 30), LocalDateTime.of(2024, 1, 4, 6, 30), SleepQuality.BAD)
        );
        BadQualityCountFunction function = new BadQualityCountFunction();
        assertEquals(2, function.apply(sessions).getValue());
    }

    @Test
    void testSleeplessNightsWithEmptyList() {
        List<SleepingSession> sessions = Collections.emptyList();
        SleeplessNightsCountFunction function = new SleeplessNightsCountFunction();
        assertEquals(0, function.apply(sessions).getValue());
    }

    @Test
    void testSleeplessNightsWithOneSleeplessNight() {
        List<SleepingSession> sessions = Arrays.asList(
            new SleepingSession(LocalDateTime.of(2024, 1, 1, 22, 0), LocalDateTime.of(2024, 1, 2, 8, 0), SleepQuality.GOOD),
            new SleepingSession(LocalDateTime.of(2024, 1, 3, 7, 0), LocalDateTime.of(2024, 1, 3, 11, 0), SleepQuality.NORMAL)
        );
        SleeplessNightsCountFunction function = new SleeplessNightsCountFunction();
        assertEquals(1, function.apply(sessions).getValue());
    }

    @Test
    void testSleeplessNightsWithDifferentMonths() {
        List<SleepingSession> sessions = Arrays.asList(
            new SleepingSession(LocalDateTime.of(2024, 1, 30, 22, 0), LocalDateTime.of(2024, 1, 31, 8, 0), SleepQuality.GOOD),
            new SleepingSession(LocalDateTime.of(2024, 2, 1, 23, 0), LocalDateTime.of(2024, 2, 2, 7, 0), SleepQuality.NORMAL),
            new SleepingSession(LocalDateTime.of(2024, 2, 3, 0, 0), LocalDateTime.of(2024, 2, 3, 6, 0), SleepQuality.BAD)
        );
        SleeplessNightsCountFunction function = new SleeplessNightsCountFunction();
        assertEquals(0, function.apply(sessions).getValue());
    }

    @Test
    void testSleeplessNightsWithNoNightSessions() {
        List<SleepingSession> sessions = Arrays.asList(
            new SleepingSession(LocalDateTime.of(2024, 1, 1, 7, 0), LocalDateTime.of(2024, 1, 1, 11, 0), SleepQuality.GOOD),
            new SleepingSession(LocalDateTime.of(2024, 1, 2, 8, 0), LocalDateTime.of(2024, 1, 2, 12, 0), SleepQuality.NORMAL)
        );
        SleeplessNightsCountFunction function = new SleeplessNightsCountFunction();
        assertEquals(2, function.apply(sessions).getValue());
    }

    @Test
    void testSleeplessNightsWithSessionStartingAfterMidnight() {
        List<SleepingSession> sessions = Arrays.asList(
            new SleepingSession(LocalDateTime.of(2024, 1, 2, 1, 0), LocalDateTime.of(2024, 1, 2, 8, 0), SleepQuality.GOOD),
            new SleepingSession(LocalDateTime.of(2024, 1, 3, 23, 0), LocalDateTime.of(2024, 1, 4, 7, 0), SleepQuality.NORMAL)
        );
        SleeplessNightsCountFunction function = new SleeplessNightsCountFunction();
        assertEquals(1, function.apply(sessions).getValue());
    }

    @Test
    void testChronotypeWithEmptySessions() {
        List<SleepingSession> sessions = Collections.emptyList();
        ChronotypeFunction function = new ChronotypeFunction();
        assertEquals("Голубь", function.apply(sessions).getValue());
    }

    @Test
    void testChronotypeOnlyOwls() {
        List<SleepingSession> sessions = Arrays.asList(
            new SleepingSession(LocalDateTime.of(2024, 1, 1, 23, 30), LocalDateTime.of(2024, 1, 2, 9, 30), SleepQuality.GOOD),
            new SleepingSession(LocalDateTime.of(2024, 1, 2, 23, 45), LocalDateTime.of(2024, 1, 3, 10, 0), SleepQuality.GOOD)
        );
        ChronotypeFunction function = new ChronotypeFunction();
        assertEquals("Сова", function.apply(sessions).getValue());
    }

    @Test
    void testChronotypeOnlyLarks() {
        List<SleepingSession> sessions = Arrays.asList(
            new SleepingSession(LocalDateTime.of(2024, 1, 1, 21, 0), LocalDateTime.of(2024, 1, 2, 6, 30), SleepQuality.GOOD),
            new SleepingSession(LocalDateTime.of(2024, 1, 2, 21, 30), LocalDateTime.of(2024, 1, 3, 6, 0), SleepQuality.GOOD)
        );
        ChronotypeFunction function = new ChronotypeFunction();
        assertEquals("Жаворонок", function.apply(sessions).getValue());
    }

    @Test
    void testChronotypeIgnoresDaySessions() {
        List<SleepingSession> sessions = Arrays.asList(
            new SleepingSession(LocalDateTime.of(2024, 1, 1, 23, 30), LocalDateTime.of(2024, 1, 2, 9, 30), SleepQuality.GOOD),
            new SleepingSession(LocalDateTime.of(2024, 1, 2, 14, 0), LocalDateTime.of(2024, 1, 2, 15, 0), SleepQuality.NORMAL),
            new SleepingSession(LocalDateTime.of(2024, 1, 2, 23, 30), LocalDateTime.of(2024, 1, 3, 9, 30), SleepQuality.GOOD)
        );
        ChronotypeFunction function = new ChronotypeFunction();
        assertEquals("Сова", function.apply(sessions).getValue());
    }
}
