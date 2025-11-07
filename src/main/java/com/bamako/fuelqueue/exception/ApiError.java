package com.bamako.fuelqueue.exception;

import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@Builder
public class ApiError {
    Instant timestamp;
    int status;
    String error;
    String message;
    String path;
    List<String> validationErrors;

    public static ApiError of(HttpStatus status, String message, String path, List<String> validationErrors) {
        return ApiError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .validationErrors(validationErrors == null ? List.of() : validationErrors)
                .build();
    }
}
