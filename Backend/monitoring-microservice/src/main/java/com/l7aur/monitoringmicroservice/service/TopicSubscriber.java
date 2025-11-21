package com.l7aur.monitoringmicroservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7aur.monitoringmicroservice.model.Device;
import com.l7aur.monitoringmicroservice.model.util.TopicMessageType;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TopicSubscriber {
    private final DeviceService deviceService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${monitoring.microservice.queue}", containerFactory = "topicListenerFactory")
    private void receiveMessage(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            TopicMessageType type = TopicMessageType.valueOf(root.get("type").asText());
            switch (type) {
                case DEVICE_CREATE:
                    deviceService.create(new Device(null, root.get("referencedId").asInt()));
                    break;
                case DEVICE_DELETE:
                    List<Integer> ids = objectMapper.convertValue(root.get("ids"), new TypeReference<>() {
                    });
                    ids.forEach(deviceService::deleteByReferencedId);
                    break;
                default:
                    System.out.println("Invalid message received");
            }
        } catch (JsonProcessingException e) {
            System.out.println("Error while processing message: " + message + "\n" + e.getMessage());
        }
    }
}
