package com.bamako.fuelqueue.domain.mapper;

import com.bamako.fuelqueue.domain.entity.ConsumptionRecord;
import com.bamako.fuelqueue.dto.response.ConsumptionRecordResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TicketMapper.class, StationMapper.class, UserMapper.class})
public interface ConsumptionRecordMapper {

    ConsumptionRecordResponse toDto(ConsumptionRecord record);
}
