package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.serializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class KeyMixIn {
    @JsonCreator
    public KeyMixIn(@JsonProperty("value") String value){

    }
}
