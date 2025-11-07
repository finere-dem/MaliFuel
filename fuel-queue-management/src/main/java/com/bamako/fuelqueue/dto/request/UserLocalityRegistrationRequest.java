package com.bamako.fuelqueue.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserLocalityRegistrationRequest {

    @NotNull
    private UUID localityId;

    private boolean primary;
}
