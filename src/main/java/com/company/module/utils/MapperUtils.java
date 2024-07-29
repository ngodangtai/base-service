package com.company.module.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;

import java.io.InputStream;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperUtils {

    @Getter
    private static final ObjectMapper objectMapper;
    private static final ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new StdDateFormat());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String writeValueAsString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            log.error(new StringBuilder("Error while writing value as string ")
                    .append(object.getClass().getSimpleName()).append(ex.getMessage()).toString(), ex);
            return StringUtils.EMPTY;
        }
    }

    public static <T> T convertValue(Object value, Class<T> className) {
        try {
            return objectMapper.convertValue(value, className);
        } catch (Exception ex) {
            log.error("Error while converting value ", ex);
            return null;
        }
    }

    public static <V> V convertValue(Object value, TypeReference<V> type) {
        try {
            return objectMapper.convertValue(value, type);
        } catch (Exception ex) {
            log.error("Error while converting value ", ex);
            return null;
        }
    }

    public static <T> T readValue(String str, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(str, valueTypeRef);
        } catch (Exception ex) {
            log.error("Error while reading value from string ", ex);
            return null;
        }
    }

    public static <T> T readValue(InputStream is, Class<T> valueType) {
        try {
            return objectMapper.readValue(is, valueType);
        } catch (Exception ex) {
            log.error("Error while reading value from input stream ", ex);
            return null;
        }
    }

    public static <T> T readValue(String str, Class<T> valueType) {
        try {
            return objectMapper.readValue(str, valueType);
        } catch (Exception ex) {
            log.error("Error while reading value ", ex);
            return null;
        }
    }

    public static <T> T copyProperties(Object value, Class<T> className) {
        try {
            return modelMapper.map(value, className);
        } catch (Exception ex) {
            log.error("Error while copy value ", ex);
            return null;
        }
    }
}
