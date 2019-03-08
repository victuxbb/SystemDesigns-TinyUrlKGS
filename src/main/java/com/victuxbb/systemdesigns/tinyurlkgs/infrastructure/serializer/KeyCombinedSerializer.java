package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.Key;

import java.io.IOException;

public class KeyCombinedSerializer {
  public static class KeyJsonSerializer extends JsonSerializer<Key> {
    @Override
    public void serialize(Key value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      gen.writeString(value.getValue());
    }
  }
  public static class KeyJsonDeserializer extends JsonDeserializer<Key> {

    @Override
    public Key deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      return new Key(p.readValueAs(String.class));
    }
  }
}
