package com.bamako.fuelqueue.service;

import java.util.UUID;

public interface QueueNotificationService {

    void notifyQueueChanged(UUID stationId);
}
