package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KeysRequest {
    private Long quantity;

    @JsonCreator
    public KeysRequest(@JsonProperty("quantity") Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }
}
