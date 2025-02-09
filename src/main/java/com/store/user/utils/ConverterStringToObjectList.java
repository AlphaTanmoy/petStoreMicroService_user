package com.store.user.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Component
public class ConverterStringToObjectList {

    private static final ObjectMapper objectMapper = createDefaultMapper();

    private static ObjectMapper createDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setTimeZone(TimeZone.getDefault());
        mapper.registerModule(new JavaTimeModule()); // ✅ Enables Java 8 Date/Time support
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // ✅ Ensures ISO-8601 format for dates
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            @Override
            public boolean hasIgnoreMarker(AnnotatedMember m) {
                return m != null && m.hasAnnotation(ConditionalJsonIgnore.class) ? false : super.hasIgnoreMarker(m);
            }
        });
        return mapper;
    }

    public static <T> List<T> getObjectList(String dataToConvert, Class<T> clazz) {
        if (dataToConvert == null || dataToConvert.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(
                    dataToConvert,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)
            );
        } catch (Exception e) {
            System.err.println("Error converting string to object list: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static <T> T getSingleObject(String dataToConvert, boolean shouldIncludeConditionalJsonIgnores, Class<T> clazz) {
        try {
            return createMapper(shouldIncludeConditionalJsonIgnores).readValue(dataToConvert, clazz);
        } catch (Exception e) {
            System.err.println("Error converting string to single object: " + e.getMessage());
            return null;
        }
    }

    public static <T> T getSingleObjectTypeTwo(String dataToConvert, Class<T> clazz, boolean shouldIncludeConditionalJsonIgnores) {
        return getSingleObject(dataToConvert, shouldIncludeConditionalJsonIgnores, clazz);
    }

    public static String convertObjectToString(Object objectToConvert, SimpleFilterProvider filter, boolean shouldIncludeConditionalJsonIgnores) {
        try {
            ObjectMapper mapper = createMapper(shouldIncludeConditionalJsonIgnores);
            return filter != null ? mapper.writer(filter).writeValueAsString(objectToConvert) : mapper.writeValueAsString(objectToConvert);
        } catch (JsonProcessingException e) {
            System.err.println("Error converting object to string: " + e.getMessage());
            return "{}";
        }
    }

    public static <T> List<T> sanitizeForOutput(Object objectToConvert, Class<T> clazz) {
        return getObjectList(convertObjectToString(objectToConvert, null, true), clazz);
    }

    public static <T> T sanitizeForOutputSingleObject(Object objectToConvert, Class<T> clazz) {
        return getSingleObject(convertObjectToString(objectToConvert, null, true), false, clazz);
    }

    private static ObjectMapper createMapper(boolean shouldIncludeConditionalJsonIgnores) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setTimeZone(TimeZone.getDefault());
        mapper.registerModule(new JavaTimeModule());  // ✅ Ensures `Instant` and `LocalDateTime` are handled correctly
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            @Override
            public boolean hasIgnoreMarker(AnnotatedMember m) {
                return (m == null || !shouldIncludeConditionalJsonIgnores || !m.hasAnnotation(ConditionalJsonIgnore.class)) && super.hasIgnoreMarker(m);
            }
        });
        return mapper;
    }
}



