package com.bamako.fuelqueue.controller;

import com.bamako.fuelqueue.dto.ticket.TicketResponse;
import com.bamako.fuelqueue.dto.ticket.TicketValidationRequest;
import com.bamako.fuelqueue.service.GerantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stations")
@RequiredArgsConstructor
public class GerantController {

    private final GerantService gerantService;

    @GetMapping("/{stationId}/tickets")
    public ResponseEntity<List<TicketResponse>> getWaitingTickets(@PathVariable UUID stationId) {
        return ResponseEntity.ok(gerantService.getWaitingTickets(stationId));
    }

    @PostMapping("/{stationId}/tickets/{ticketId}/scan-validate")
    public ResponseEntity<TicketResponse> validateTicket(@PathVariable UUID stationId,
                                                         @PathVariable UUID ticketId,
                                                         @Valid @RequestBody TicketValidationRequest request) {
        return ResponseEntity.ok(gerantService.validateTicket(stationId, ticketId, request));
    }
}
