package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.dto.request.TicketCreateRequest;
import com.bamako.fuelqueue.dto.response.TicketResponse;

import java.util.UUID;

public interface TicketService {

    TicketResponse createTicket(TicketCreateRequest request);

    TicketResponse getTicket(UUID ticketId);

    void cancelTicket(UUID ticketId);
}
