package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.Key;

public class ObjectMapperFactory {
    public static ObjectMapper build() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.addMixIn(Key.class, KeyMixIn.class);
        return objectMapper;
    }
}
