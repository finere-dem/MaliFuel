package com.bamako.fuelqueue.service.impl;

import com.bamako.fuelqueue.service.QueueNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueNotificationServiceImpl implements QueueNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void notifyQueueChanged(UUID stationId) {
        messagingTemplate.convertAndSend(String.format("/topic/stations/%s/queue", stationId),
            Map.of("event", "QUEUE_UPDATED", "stationId", stationId.toString()));
    }
}
