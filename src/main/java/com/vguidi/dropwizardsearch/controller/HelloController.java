package com.vguidi.dropwizardsearch.controller;

import com.codahale.metrics.annotation.Timed;
import com.vguidi.dropwizardsearch.model.Hello;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
public class HelloController {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public HelloController(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Hello sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.orElse(defaultName));
        return new Hello(counter.incrementAndGet(), value);
    }
}