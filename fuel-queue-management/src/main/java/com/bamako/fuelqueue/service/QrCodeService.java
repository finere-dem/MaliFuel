package com.bamako.fuelqueue.service;

public interface QrCodeService {

    String generateTicketQr(String ticketId, String stationId);
}
