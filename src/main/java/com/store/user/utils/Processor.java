package com.store.user.utils;

import com.store.user.error.BadRequestException;
import com.store.user.response.FilterOption;
import com.store.user.config.KeywordsAndConstants;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class Processor {

    private DateUtil dateUtil;

    public String processFilter(String value, String filterName, List<FilterOption> toRetFilterOption) {
        if (value == null) {
            return "%";
        }

        try {

        } catch (Exception ex) {
            throw new BadRequestException("Please provide a valid " + filterName);
        }

        String processedValue = value.contains(",") ?
                String.join("|", value.split(",")) :
                "%" + URLDecoder.decode(value, StandardCharsets.UTF_8).toLowerCase() + "%";

        toRetFilterOption.add(new FilterOption(filterName, value, value));
        return processedValue;
    }

    public String processSimpleFilter(String value, String filterName, List<FilterOption> toRetFilterOption) {
        if (value == null) {
            return "%";
        }
        toRetFilterOption.add(new FilterOption(filterName, value, value));
        return "%" + URLDecoder.decode(value, StandardCharsets.UTF_8).toLowerCase() + "%";
    }

    public ZonedDateTime processDateFilter(String dateValue, String filterName, List<FilterOption> toRetFilterOption) {
        if (dateValue == null) {
            return null;
        }

        Optional<ZonedDateTime> parsedDate = dateUtil.getZonedDateTimeFromString(dateValue, KeywordsAndConstants.DATE_TIME_FORMAT_FROM_FRONTEND);
        if (parsedDate.isEmpty()) {
            throw new BadRequestException("Please provide valid " + filterName);
        }

        toRetFilterOption.add(new FilterOption(filterName, dateValue, dateValue));
        return parsedDate.get();
    }

}
