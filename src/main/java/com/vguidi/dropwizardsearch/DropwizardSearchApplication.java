package com.vguidi.dropwizardsearch;

import com.vguidi.dropwizardsearch.api.Hello;
import com.vguidi.dropwizardsearch.health.HelloHealthCheck;
import com.vguidi.dropwizardsearch.resources.HelloResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Environment;

public class DropwizardSearchApplication extends Application<DropwizardSearchConfiguration> {
    public static void main(String[] args) throws Exception {
        new DropwizardSearchApplication().run(args);
    }

    @Override
    public void run(DropwizardSearchConfiguration configuration, Environment environment) throws Exception {
        HelloResource resource = new HelloResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);
        HelloHealthCheck healthCheck = new HelloHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("hello", healthCheck);
    }

}
