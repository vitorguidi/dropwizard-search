package com.vguidi.dropwizardsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.HealthReportResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.vguidi.dropwizardsearch.core.HelloService;
import com.vguidi.dropwizardsearch.health.HelloHealthCheck;
import com.vguidi.dropwizardsearch.controller.HelloController;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.kafka.KafkaConsumerBundle;
import io.dropwizard.kafka.KafkaConsumerFactory;
import io.dropwizard.kafka.KafkaProducerBundle;
import io.dropwizard.kafka.KafkaProducerFactory;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.internals.NoOpConsumerRebalanceListener;
import org.apache.kafka.clients.producer.Producer;
import org.elasticsearch.client.RestClient;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class DropwizardSearchApplication extends Application<DropwizardSearchConfiguration> {
    public static void main(String[] args) throws Exception {
        new DropwizardSearchApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<DropwizardSearchConfiguration> bootstrap) {
        bootstrap.addBundle(kafkaProducer);
        bootstrap.addBundle(kafkaConsumer);
    }

    private final KafkaProducerBundle<String, String, DropwizardSearchConfiguration> kafkaProducer =
            new KafkaProducerBundle<>(List.of("example-topic")) {
        @Override
        public KafkaProducerFactory<String, String> getKafkaProducerFactory(DropwizardSearchConfiguration configuration) {
            return configuration.getKafkaProducerFactory();
        }
    };

    private final KafkaConsumerBundle<String, String, DropwizardSearchConfiguration> kafkaConsumer =
            new KafkaConsumerBundle<>(
                    List.of("example-topic"), new NoOpConsumerRebalanceListener()) {
                @Override
                public KafkaConsumerFactory<String, String> getKafkaConsumerFactory(DropwizardSearchConfiguration configuration) {
                    return configuration.getKafkaConsumerFactory();
                }
            };

    @Override
    public void run(DropwizardSearchConfiguration configuration, Environment environment) throws Exception {
        HelloController resource = new HelloController(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);
        HelloHealthCheck healthCheck = new HelloHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("hello", healthCheck);
        ScheduledExecutorService consumerExecutorService = environment.lifecycle()
                .scheduledExecutorService("kafka-consumer").build();
        Producer<String, String> producer = kafkaProducer.getProducer();
        Consumer<String, String> consumer = kafkaConsumer.getConsumer();
        consumer.subscribe(List.of("example-topic"));
        HelloService helloService = new HelloService(consumerExecutorService, consumer, producer);
        RestClient restClient = RestClient
                .builder(HttpHost.create(configuration.getElasticSearchEndpoint()))
                .build();

// Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

// And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);
        BooleanResponse resp = esClient.ping();
        System.out.println("ES response = " + resp.value());
      }

}