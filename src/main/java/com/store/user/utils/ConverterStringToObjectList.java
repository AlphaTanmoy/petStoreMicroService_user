package com.store.user.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class ConverterStringToObjectList {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setTimeZone(TimeZone.getDefault());
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            @Override
            public boolean hasIgnoreMarker(AnnotatedMember m) {
                if (m != null && m.hasAnnotation(ConditionalJsonIgnore.class)) {
                    return false;
                }
                return super.hasIgnoreMarker(m);
            }
        });
        objectMapper.findAndRegisterModules();
    }

    public static <T> List<T> getObjectList(String dataToConvert, Class<T> clazz) {
        if (dataToConvert.isEmpty()) {
            return new ArrayList<>();
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.setTimeZone(TimeZone.getDefault());

        try {
            return mapper.readValue(
                    dataToConvert,
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static <T> T getSingleObject(String dataToConvert, boolean shouldIncludeConditionalJsonIgnores, Class<T> clazz) {
        try {
            ObjectMapper mapper = createMapper(shouldIncludeConditionalJsonIgnores);
            return mapper.readValue(dataToConvert, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T getSingleObjectTypeTwo(String dataToConvert, Class<T> clazz, boolean shouldIncludeConditionalJsonIgnores) {
        return getSingleObject(dataToConvert, shouldIncludeConditionalJsonIgnores, clazz);
    }

    public static String convertObjectToString(Object objectToConvert, SimpleFilterProvider filter, boolean shouldIncludeConditionalJsonIgnores) {
        try {
            ObjectMapper mapper = createMapper(shouldIncludeConditionalJsonIgnores);
            return mapper.writer(filter).writeValueAsString(objectToConvert);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }

    public static <T> List<T> sanitizeForOutput(Object objectToConvert, Class<T> clazz) {
        return getObjectList(
                convertObjectToString(objectToConvert, null, true),
                clazz
        );
    }

    public static <T> T sanitizeForOutputSingleObject(Object objectToConvert, Class<T> clazz) {
        return getSingleObject(
                convertObjectToString(objectToConvert, null, true),
                false,
                clazz
        );
    }

    private static ObjectMapper createMapper(boolean shouldIncludeConditionalJsonIgnores) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setTimeZone(TimeZone.getDefault());
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            @Override
            public boolean hasIgnoreMarker(AnnotatedMember m) {
                if (m != null && shouldIncludeConditionalJsonIgnores && m.hasAnnotation(ConditionalJsonIgnore.class)) {
                    return false;
                }
                return super.hasIgnoreMarker(m);
            }
        });
        return mapper.findAndRegisterModules();
    }
}


