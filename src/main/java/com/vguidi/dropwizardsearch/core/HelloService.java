package com.vguidi.dropwizardsearch.core;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;

import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HelloService {
    private final Producer<String, String> producer;
    private final Consumer<String, String> consumer;
    private final ScheduledExecutorService executorService;

    public HelloService(
            ScheduledExecutorService executorService,
            Consumer<String, String> consumer,
            Producer<String, String> producer)
    {
        this.executorService = executorService;
        this.consumer = consumer;
        this.producer = producer;
        this.executorService.scheduleAtFixedRate( () -> {
            System.out.println("Within thread run");

            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
            Iterator<ConsumerRecord<String, String>> iterator = consumerRecords.iterator();
            while (iterator.hasNext()) {
                ConsumerRecord<String, String> consumerRecord = iterator.next();
                try {
                    String key = consumerRecord.key();
                    String value = consumerRecord.value();
                    String message = String.format("Key = %s, Value = %s", key, value);
                    System.out.println(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2, 2, TimeUnit.SECONDS);
    }
}
