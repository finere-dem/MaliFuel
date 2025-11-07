package com.bamako.fuelqueue.service;

import com.bamako.fuelqueue.domain.dto.ticket.CreateTicketRequest;
import com.bamako.fuelqueue.domain.dto.ticket.QueueUpdateMessage;
import com.bamako.fuelqueue.domain.dto.ticket.TicketResponse;
import com.bamako.fuelqueue.domain.dto.ticket.TicketSummaryDto;
import java.util.List;
import java.util.UUID;

public interface TicketService {

    TicketResponse createTicket(CreateTicketRequest request);

    TicketResponse getTicket(UUID ticketId);

    void cancelTicket(UUID ticketId);

    List<TicketSummaryDto> getStationTickets(UUID stationId);

    TicketResponse validateTicket(UUID stationId, UUID ticketId, String qrContent);

    QueueUpdateMessage buildQueueSnapshot(UUID stationId);
}
