package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.dto.response.AnalyticsResponse;

import java.time.OffsetDateTime;

public interface AnalyticsService {

    AnalyticsResponse getConsumptionAnalytics(OffsetDateTime from, OffsetDateTime to);
}
