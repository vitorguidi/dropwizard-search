package com.vguidi.dropwizardsearch;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;

import javax.validation.constraints.NotEmpty;

public class DropwizardSearchConfiguration extends Configuration {
    @NotEmpty
    private String template = "Hey, %s!";
    @NotEmpty
    private String defaultName = "Joe";

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String message) {
        this.defaultName = message;
    }
}
