package com.bamako.fuelqueue.domain.dto.ticket;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class QueueUpdateMessage {
    UUID stationId;
    List<TicketSummaryDto> tickets;
}
