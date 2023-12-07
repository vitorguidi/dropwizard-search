package com.vguidi.dropwizardsearch;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import io.dropwizard.kafka.KafkaProducerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

    @Valid
    @NotNull
    @JsonProperty("producer")
    private KafkaProducerFactory<String, String> kafkaProducerFactory;

    @JsonProperty
    public KafkaProducerFactory<String, String> getKafkaProducerFactory() {
        return kafkaProducerFactory;
    }

}
