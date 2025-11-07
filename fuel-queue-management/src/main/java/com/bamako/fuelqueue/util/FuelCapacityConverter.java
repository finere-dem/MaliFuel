package com.bamako.fuelqueue.util;

import com.bamako.fuelqueue.domain.enumeration.FuelType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

@Converter
public class FuelCapacityConverter implements AttributeConverter<Map<FuelType, Integer>, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<FuelType, Integer> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize fuel capacity map", e);
        }
    }

    @Override
    public Map<FuelType, Integer> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return new EnumMap<>(FuelType.class);
        }
        try {
            return OBJECT_MAPPER.readValue(dbData, OBJECT_MAPPER.getTypeFactory()
                .constructMapType(EnumMap.class, FuelType.class, Integer.class));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to deserialize fuel capacity map", e);
        }
    }
}
