package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.dto.request.TicketValidationRequest;
import com.bamako.fuelqueue.dto.response.QueueTicketResponse;

import java.util.List;
import java.util.UUID;

public interface GerantService {

    List<QueueTicketResponse> getWaitingTickets(UUID stationId);

    QueueTicketResponse validateTicket(UUID stationId, UUID ticketId, TicketValidationRequest request);
}
