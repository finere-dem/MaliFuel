package com.bamako.fuelqueue.util;

import com.bamako.fuelqueue.enums.FuelType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Converter
public class FuelCapacityConverter implements AttributeConverter<Map<FuelType, Integer>, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Integer>> TYPE_REFERENCE = new TypeReference<>() {
    };

    @Override
    public String convertToDatabaseColumn(Map<FuelType, Integer> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "{}";
        }
        Map<String, Integer> serialized = attribute.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().name(), Map.Entry::getValue,
                        (left, right) -> right, LinkedHashMap::new));
        try {
            return OBJECT_MAPPER.writeValueAsString(serialized);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to serialize fuel capacity map", e);
        }
    }

    @Override
    public Map<FuelType, Integer> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return new LinkedHashMap<>();
        }
        try {
            Map<String, Integer> raw = OBJECT_MAPPER.readValue(dbData, TYPE_REFERENCE);
            return raw.entrySet().stream()
                    .collect(Collectors.toMap(entry -> FuelType.valueOf(entry.getKey()), Map.Entry::getValue,
                            (left, right) -> right, LinkedHashMap::new));
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to deserialize fuel capacity map", e);
        }
    }
}
