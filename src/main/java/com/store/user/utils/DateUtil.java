package com.store.user.utils;

import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class DateUtil {
    public Optional<ZonedDateTime> getZonedDateTimeFromString(String stringRep, String format) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(stringRep, dateTimeFormatter);
            return Optional.of(zonedDateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<ZonedDateTime> getLocalDateTimeFromStringUsingIsoFormatServerTimeZone(String stringRep) {
        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(stringRep, DateTimeFormatter.ISO_DATE_TIME);
            return Optional.of(zonedDateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    private String sanitizeDateTimeString(String input) {
        return input;
    }

    private String sanitizeDateTimeStringWithTRemoval(String input, boolean removeT) {
        return removeT ? input.replace("T", " ") : input;
    }
}
