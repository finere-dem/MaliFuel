package com.bamako.fuelqueue.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddLocalityRequest {

    @NotNull
    private UUID localityId;

    private boolean primary;
}
