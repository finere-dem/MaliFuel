package com.bamako.fuelqueue.controller;

import com.bamako.fuelqueue.domain.dto.ticket.TicketResponse;
import com.bamako.fuelqueue.domain.dto.ticket.TicketSummaryDto;
import com.bamako.fuelqueue.domain.dto.ticket.TicketValidationRequest;
import com.bamako.fuelqueue.service.TicketService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stations")
public class GerantController {

    private final TicketService ticketService;

    @GetMapping("/{stationId}/tickets")
    @PreAuthorize("hasAnyRole('GERANT','ADMIN')")
    public ResponseEntity<List<TicketSummaryDto>> getStationTickets(@PathVariable UUID stationId) {
        return ResponseEntity.ok(ticketService.getStationTickets(stationId));
    }

    @PostMapping("/{stationId}/tickets/{ticketId}/scan-validate")
    @PreAuthorize("hasAnyRole('GERANT','ADMIN')")
    public ResponseEntity<TicketResponse> validateTicket(
            @PathVariable UUID stationId,
            @PathVariable UUID ticketId,
            @Valid @RequestBody TicketValidationRequest request) {
        TicketResponse response = ticketService.validateTicket(stationId, ticketId, request.getQrContent());
        return ResponseEntity.ok(response);
    }
}
