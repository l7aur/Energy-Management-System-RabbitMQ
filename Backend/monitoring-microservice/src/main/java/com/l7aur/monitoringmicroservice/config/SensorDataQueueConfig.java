package com.l7aur.monitoringmicroservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SensorDataQueueConfig {

    @Value("${sensor.data.queue.name}")
    private String dataQueueName;

    @Bean
    public Queue sensorDataQueue() {
        return new Queue(dataQueueName, true);
    }

    @Bean
    public RabbitAdmin queueAdmin(ConnectionFactory rabbitConnectionFactory) {
        return new RabbitAdmin(rabbitConnectionFactory);
    }
}
