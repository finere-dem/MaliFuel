package com.bamako.fuelqueue.domain.mapper;

import com.bamako.fuelqueue.domain.entity.Ticket;
import com.bamako.fuelqueue.dto.response.QueueTicketResponse;
import com.bamako.fuelqueue.dto.response.TicketResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StationMapper.class, VehicleMapper.class})
public interface TicketMapper {

    TicketResponse toDto(Ticket ticket);

    @Mapping(target = "userFullName", expression = "java(ticket.getUser().getFirstName() + \" \" + ticket.getUser().getLastName())")
    @Mapping(target = "userPhone", source = "user.phone")
    @Mapping(target = "vehiclePlateNumber", source = "vehicle.plateNumber")
    QueueTicketResponse toQueueDto(Ticket ticket);
}
