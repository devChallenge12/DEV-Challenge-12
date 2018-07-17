package it.devchallenge.distdb.util;


import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class FromStringConverter {
    public static final String TIMESTAMP_PATTERN = "yyyyMMdd'T'HHmmssX";
    private static final String TIMESTAMP_PATTERN_FULL = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    public static final String TO_STRING_PATTERN = "yyyyMMdd'T'HHmmss'Z'";

    private FromStringConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static LocalDateTime getLocalDateTime(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);
        LocalDateTime localDateTime;
        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(timestamp, formatter);
            localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Z")).toLocalDateTime();
        } catch (DateTimeParseException e) {
            log.debug("wrong timestamp: " + timestamp, e);
            try {
                ZonedDateTime zonedDateTime2 = ZonedDateTime.parse(timestamp, DateTimeFormatter.ofPattern
                        (TIMESTAMP_PATTERN_FULL));
                localDateTime = zonedDateTime2.withZoneSameInstant(ZoneId.of("Z")).toLocalDateTime();
                return localDateTime;
            } catch (DateTimeParseException e2) {
                log.warn("wrong timestamp: " + timestamp, e2);
                throw new WebApplicationException("wrong timestamp", Response.Status.BAD_REQUEST);
            }
        }
        return localDateTime;
    }

    public static String from(LocalDateTime from) {
        return from.format(DateTimeFormatter.ofPattern(TO_STRING_PATTERN));
    }
}