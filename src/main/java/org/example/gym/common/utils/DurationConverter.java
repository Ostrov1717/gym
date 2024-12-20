package org.example.gym.common.utils;

import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class DurationConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration duration) {
        if (duration == null) {
            log.debug("Duration is null; returning null for database column.");
            throw new IllegalArgumentException("Duration is null; returning null for database column.");
        }
        long seconds = duration.getSeconds();
        log.debug("Converting Duration [{}] to seconds [{}] for database column.", duration, seconds);
        return seconds;
    }

    @Override
    public Duration convertToEntityAttribute(Long seconds) {
        if (seconds == null) {
            log.debug("Database column is null; returning null for Duration.");
            throw new IllegalArgumentException("Database column is null; returning null for Duration.");
        }
        Duration duration = Duration.ofSeconds(seconds);
        log.debug("Converting seconds [{}] from database column to Duration [{}].", seconds, duration);
        return duration;
    }

}
