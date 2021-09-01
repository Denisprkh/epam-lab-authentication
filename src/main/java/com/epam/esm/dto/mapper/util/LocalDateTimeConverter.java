package com.epam.esm.dto.mapper.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateTimeConverter {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public String convertLocalDateTimeToISOFormat(LocalDateTime timestamp) {
        OffsetDateTime offsetDateTime = OffsetDateTime.of(timestamp, ZoneOffset.UTC);
        return offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public LocalDateTime convertStringToLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDateTime.parse(date, formatter);
    }

}
