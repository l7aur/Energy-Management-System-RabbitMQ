package com.l7aur.devicemicroservice.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7aur.devicemicroservice.config.TopicConfig;
import com.l7aur.devicemicroservice.model.util.TopicMessageType;
import lombok.AllArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TopicPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final TopicConfig config;

    public void requestMonitoringServiceNewDevice(Integer id) {
        record Message(TopicMessageType type, Integer referencedId) {
        }

        try {
            sendMessageToMonitoringMicroservice(objectMapper.writeValueAsString(
                    new Message(TopicMessageType.DEVICE_CREATE, id))
            );
        } catch (JsonProcessingException e) {
            System.out.println("Error while sending message from device microservice: " + e.getMessage());
        }
    }

    public void requestMonitoringServiceDeleteDevices(List<Integer> ids) {
        record Message(TopicMessageType type, List<Integer> ids) {
        }

        try {
            sendMessageToMonitoringMicroservice(objectMapper.writeValueAsString(
                    new Message(TopicMessageType.DEVICE_DELETE, ids))
            );
        } catch (JsonProcessingException e) {
            System.out.println("Error while sending message from device microservice: " + e.getMessage());
        }
    }

    private void sendMessageToMonitoringMicroservice(String message) {
        try {
            rabbitTemplate.convertAndSend(config.getTOPIC_EXCHANGE_NAME(), config.getMONITORING_ROUTING_KEY(), message);
        } catch (AmqpException e) {
            System.out.println("Error while sending user message: " + e.getMessage());
        }
    }
}
