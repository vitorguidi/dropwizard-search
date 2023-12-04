package com.vguidi.dropwizardsearch.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hello {
    private long id;
    private String content;

    public Hello() {
        // Jackson deserialization
    }

    public Hello(long id, String content) {
        this.id = id;
        this.content = content;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }
}
