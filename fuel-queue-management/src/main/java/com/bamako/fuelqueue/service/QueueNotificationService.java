package com.bamako.fuelqueue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcastQueueUpdate(UUID stationId) {
        Map<String, Object> payload = Map.of(
                "stationId", stationId,
                "timestamp", Instant.now().toString()
        );
        messagingTemplate.convertAndSend(queueDestination(stationId), payload);
    }

    public String queueDestination(UUID stationId) {
        return "/topic/stations/" + stationId;
    }
}
