package org.viralscale.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.viralscale.common.models.DataModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JSONUtils<T> {
    public static <T> String toJsonString(T input) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper.writeValueAsString(input);
    }

    public static <T> T fromJson(byte[] rawData, Class<T> tClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String data = new String(rawData, StandardCharsets.UTF_8);

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper.readValue(data, tClass);
    }
}
