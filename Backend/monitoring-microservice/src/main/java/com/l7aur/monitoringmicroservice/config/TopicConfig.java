package com.l7aur.monitoringmicroservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {

    @Value("${topic.exchange.name}")
    private String TOPIC_EXCHANGE_NAME;

    @Value("${monitoring.microservice.queue}")
    private String MONITORING_QUEUE_NAME;

    @Value("${monitoring.routing.key}")
    private String MONITORING_ROUTING_KEY;

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Queue monitoringMicroserviceQueue() {
        return new Queue(MONITORING_QUEUE_NAME, true);
    }

    @Bean
    public Binding binding(TopicExchange topicExchange, Queue monitoringMicroserviceQueue) {
        return BindingBuilder
                .bind(monitoringMicroserviceQueue)
                .to(topicExchange)
                .with(MONITORING_ROUTING_KEY);
    }
}
