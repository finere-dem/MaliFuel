package com.bamako.fuelqueue.domain.mapper;

import com.bamako.fuelqueue.domain.entity.User;
import com.bamako.fuelqueue.domain.entity.UserLocality;
import com.bamako.fuelqueue.dto.response.UserLocalityResponse;
import com.bamako.fuelqueue.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {VehicleMapper.class, LocalityMapper.class})
public interface UserMapper {

    UserResponse toDto(User user);

    @Mapping(target = "primary", source = "primaryLocality")
    @Mapping(target = "locality", source = "locality")
    UserLocalityResponse toDto(UserLocality locality);
}
